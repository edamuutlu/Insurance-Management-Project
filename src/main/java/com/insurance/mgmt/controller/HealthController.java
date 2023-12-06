package com.insurance.mgmt.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.entity.Health;
import com.insurance.mgmt.entity.HealthInsurance;
import com.insurance.mgmt.entity.Jobs;
import com.insurance.mgmt.entity.Kdv;
import com.insurance.mgmt.service.CarInsuranceService;
import com.insurance.mgmt.service.CustomerService;
import com.insurance.mgmt.service.HealthInsuranceService;
import com.insurance.mgmt.service.HealthService;
import com.insurance.mgmt.service.HomeInsuranceService;
import com.insurance.mgmt.service.JobsService;
import com.insurance.mgmt.service.KdvService;
import com.insurance.mgmt.util.CalculateMethods;

import jakarta.validation.Valid;

@Controller
public class HealthController {

	@Autowired
	HealthService healthService;

	@Autowired
	CustomerService customerService;

	@Autowired
	CarInsuranceService carInsuranceService;

	@Autowired
	HomeInsuranceService homeInsuranceService;

	@Autowired
	HealthInsuranceService healthInsuranceService;

	@Autowired
	KdvService kdvService;

	@Autowired
	private JobsService jobsService;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	private static final Logger log = LoggerFactory.getLogger(HealthController.class);

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping(path = "/healthInsuranceForm")
	public String healthInsuranceForm(@RequestParam(value = "customerId", required = false) int idParam, Model model) {

		model.addAttribute("customerId", idParam);
		return "healthInsuranceForm";
	}

	@PostMapping("/healthInfoRegister")
	public String healthInfoRegister(@Valid @ModelAttribute Health health, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes, @RequestParam(value = "customerId", required = false) int idParam) {

		if (bindingResult.hasErrors()) {
			log.info(">> health : {}", health.toString());
			model.addAttribute("customerId", health.getCustomerId());

			return "healthInsuranceForm";
		}
		model.addAttribute("Healths", healthService.getAllHealths());

		HealthInsurance insurance = new HealthInsurance();
		insurance.setCustomerId(idParam);

		LocalDateTime now = LocalDateTime.now();
		insurance.setStartDate(now.format(formatter));
		LocalDateTime endDate = now.plusDays(health.getPeriod());
		insurance.setEndDate(endDate.format(formatter));

		// Sağlık bilgilerinin geçerlilik süresi set edilmektedir
		LocalDateTime deadlineDate = now.plusDays(30);
		health.setDeadline(deadlineDate.format(formatter));

		Kdv kdv = kdvService.getProductTypeById(3);
		int kdvRate = kdv.getKdvRate();
		insurance.setKdv(kdvRate);

		Jobs job = jobsService.getJobById(health.getJob());
		int riskFactor = job.getRiskFactor();
		double offer = CalculateMethods.calculateHealthInsurance(health, kdvRate, riskFactor);
		insurance.setOffer(offer);

		insurance.setStatus(1);

		// Aynı kişiye ait sağlık bilgisi kontrolü
		List<Health> filteredList = healthService.findByForWhoAndStatus(health.getForWho(), 1);

		if (!filteredList.isEmpty()) {
			model.addAttribute("showHealthAlert", true);
			model.addAttribute("customerId", idParam);
			model.addAttribute("health", health);

			return "healthInsuranceForm";
		}

		insurance.setInsuranceType("Health");
		insurance.setResult("Canceled"); // Default olarak canceled yazdırılmaktadır
		insurance.setPeriod(health.getPeriod());

		health.setCustomerId(idParam);
		health.setStatus(1);
		healthService.save(health);

		insurance.setHealthId(health.getHealthId());
		healthInsuranceService.save(insurance);

		Customer customer = customerService.getCustomerById(health.getCustomerId());
		redirectAttributes.addFlashAttribute("customer", customer);
		redirectAttributes.addFlashAttribute("health", health);
		redirectAttributes.addFlashAttribute("insurance", insurance);

		return "redirect:/healthInsuranceCalculate/" + insurance.getInsuranceId();
	}

	@RequestMapping(path = { "/healthInsuranceCalculate/{insuranceId}",
			"/healthInsuranceCalculate/{healthInsuranceCalculate}/{admin}" }, method = RequestMethod.GET)
	public String healthInsuranceCalculate(@PathVariable("insuranceId") int insuranceId,
			@PathVariable(required = false) String admin, Model model) {

		HealthInsurance insurance = healthInsuranceService.getInsuranceById(insuranceId);
		Health health = healthService.getHealthById(insurance.getHealthId());
		Customer customer = customerService.getCustomerById(health.getCustomerId());

		if (admin == null) {
			model.addAttribute("username", customer.getUsername());
		} else if (admin.equals("admin")) {
			model.addAttribute("username", "admin");
		}

		model.addAttribute("health", health);
		model.addAttribute("insurance", insurance);
		model.addAttribute("customer", customer);
		return "healthInsuranceCalculate";
	}

	@GetMapping("/healthInfoList/{customerId}")
	public ModelAndView healthInfoList(@PathVariable("customerId") int customerId, Model model) {

		List<Health> healthInfos = healthService.findByStatusAndCustomerId(1, customerId);
		List<HealthInsurance> insurances = healthInsuranceService.findByStatusAndCustomerIdAndResult(1, customerId,
				"Accepted");
		ArrayList<Health> expiredHealthInsurance = new ArrayList<>();

		// Poliçenin süresinin bitip bitmediğini kontrol etme
		LocalDateTime now = LocalDateTime.now();
		for (Health health : healthInfos) {
			for (HealthInsurance insurance : insurances) {
				LocalDateTime endDateTime = LocalDateTime.parse(insurance.getEndDate(), formatter);

				if (now.isAfter(endDateTime)) {
					expiredHealthInsurance.add(health);
					model.addAttribute("showExpiredAlert", true);
					insurance.setResult("Expired");
					healthInsuranceService.save(insurance);
				}
			}
		}

		// Sağlık bilgilerini güncelleme süresinin bitip bitmediğini kontrol etme
		for (Health health : healthInfos) {
			LocalDateTime deadlineDate = LocalDateTime.parse(health.getDeadline(), formatter);

			if (now.isAfter(deadlineDate)) {
				model.addAttribute("showDeadlineAlert", true);
				model.addAttribute("healthId", health.getHealthId());
			}
		}

		model.addAttribute("expiredHealthInsurance", expiredHealthInsurance);
		return new ModelAndView("healthInfoList", "health", healthInfos);
	}

	@RequestMapping(path = { "/healthInsuranceEditForm/{insuranceId}",
			"/healthInsuranceEditForm/{insuranceId}/{admin}" }, method = RequestMethod.GET)
	public String healthInsuranceEditForm(@PathVariable("insuranceId") int insuranceId,
			@PathVariable(required = false) String admin, Model model, RedirectAttributes redirectAttributes) {

		HealthInsurance insurance = healthInsuranceService.getInsuranceById(insuranceId);
		Health health = healthService.getHealthById(insurance.getHealthId());

		// Devam eden bir sigorta var mı kontrolü
		List<HealthInsurance> insurances = healthInsuranceService.findByStatusAndResultAndHealthId(1, "Accepted",
				insurance.getHealthId());
		if (!insurances.isEmpty()) {
			redirectAttributes.addFlashAttribute("showText", true);
			model.addAttribute("insurance", insurances);
			return "redirect:/seeHealthInsuranceDetails/" + insurance.getHealthId();
		}

		LocalDateTime now = LocalDateTime.now();
		insurance.setEndDate(now.format(formatter));
		insurance.setResult("Canceled");
		healthInsuranceService.save(insurance);
		System.out.print(insurance.getPeriod());
		System.out.print(health.getJob());

		model.addAttribute("insurance", insurance);
		model.addAttribute("health", health);

		List<Jobs> jobs = jobsService.getAllJobs();
		model.addAttribute(jobs);

		return "healthInsuranceEdit";
	}

	@GetMapping("/healthInsuranceUpdate/{healthId}")
	public String healthInsuranceUpdate(@PathVariable("healthId") int healthId, Model model) {

		Health health = healthService.getHealthById(healthId);
		model.addAttribute(health);

		List<HealthInsurance> insurances = healthInsuranceService.findByStatusAndResultAndHealthId(1, "Accepted",
				healthId);
		for (HealthInsurance insurance : insurances) {
			insurance.setResult("Canceled");
			LocalDateTime now = LocalDateTime.now();
			insurance.setEndDate(now.format(formatter));
			healthInsuranceService.save(insurance);
		}

		List<Jobs> jobs = jobsService.getAllJobs();
		model.addAttribute(jobs);

		return "healthInsuranceEdit";
	}

	@PostMapping("/editHealthInfo")
	public String editHealthInfo(@ModelAttribute Health health, Model model, RedirectAttributes redirectAttributes) {

		Health oldHealth = healthService.getHealthById(health.getHealthId());
		Customer customer = customerService.getCustomerById(health.getCustomerId());

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endDate = now.plusDays(health.getPeriod());

		Kdv kdv = kdvService.getProductTypeById(3);
		int kdvRate = kdv.getKdvRate();
		health.setForWho(oldHealth.getForWho()); // kimin için sigorta yaptırılacağı formda değiştirilmemektedir
		Jobs job = jobsService.getJobById(health.getJob());
		int riskFactor = job.getRiskFactor();
		double offer = CalculateMethods.calculateHealthInsurance(health, kdvRate, riskFactor);

		HealthInsurance newInsurance = new HealthInsurance();
		newInsurance.setInsuranceType("Health");
		newInsurance.setCustomerId(health.getCustomerId());
		newInsurance.setHealthId(health.getHealthId());
		newInsurance.setStartDate(now.format(formatter));
		newInsurance.setEndDate(endDate.format(formatter));
		newInsurance.setKdv(kdvRate);
		newInsurance.setPeriod(health.getPeriod());
		newInsurance.setOffer(offer);
		newInsurance.setStatus(1);
		healthInsuranceService.save(newInsurance);

		// Sağlık bilgilerinin geçerlilik süresi set edilmektedir
		LocalDateTime deadlineDate = now.plusDays(30);
		health.setDeadline(deadlineDate.format(formatter));
		health.setStatus(1);
		healthService.save(health);

		model.addAttribute("insurance", newInsurance);
		model.addAttribute("health", health);
		model.addAttribute("customer", customer);
		return "redirect:/healthInsuranceCalculate/" + newInsurance.getInsuranceId();
	}

	@RequestMapping("/deleteHealthInfo/{id}")
	public String deleteHealthInfo(@PathVariable("id") int healthId, Model model) {

		Health health = healthService.getHealthById(healthId);
		List<HealthInsurance> insurances = healthInsuranceService.findByStatusAndHealthId(1, healthId);
		if (!(insurances.isEmpty())) {
			for (HealthInsurance insurance : insurances) {
				insurance.setStatus(0);
				insurance.setResult("Canceled");
				LocalDateTime now = LocalDateTime.now();
				insurance.setEndDate(now.format(formatter));
				healthInsuranceService.save(insurance);
			}
		}
		health.setStatus(0);
		healthService.save(health);
		Customer customer = customerService.getCustomerById(health.getCustomerId());
		return "redirect:/userPage/" + customer.getUsername();
	}

	@RequestMapping("/deleteHealthInsurance/{id}")
	public String deleteHealthInsurance(@PathVariable("id") int id, Model model) {

		HealthInsurance insurance = healthInsuranceService.getInsuranceById(id);
		insurance.setResult("Canceled");
		LocalDateTime now = LocalDateTime.now();
		String end_date = now.format(formatter);
		insurance.setEndDate(end_date);
		healthInsuranceService.save(insurance);
		return "redirect:/seeHealthInsuranceDetails/" + insurance.getHealthId();
	}

	@RequestMapping(path = { "/seeHealthInsuranceDetails/{id}",
			"/seeHealthInsuranceDetails/{id}/{admin}" }, method = RequestMethod.GET)
	public String seeHealthInsuranceDetails(@PathVariable("id") int healthId,
			@PathVariable(required = false) String admin, Model model) {

		List<HealthInsurance> insurances = healthInsuranceService.findByStatusAndHealthId(1, healthId);
		Health health = healthService.getHealthById(healthId);
		Customer customer = customerService.getCustomerById(health.getCustomerId());

		for (HealthInsurance insurance : insurances) {
			healthInsuranceService.save(insurance);
			model.addAttribute("insuranceId", insurance.getInsuranceId());
		}

		if (insurances.isEmpty()) {
			model.addAttribute("showAbsent", true);
			model.addAttribute("insurance", insurances);
			model.addAttribute("customer", customer);
			return "seeHealthInsuranceDetails";
		}

		if (admin == null) {
			model.addAttribute("username", customer.getUsername());
		} else if (admin.equals("admin")) {
			model.addAttribute("username", "admin");
		}

		model.addAttribute("customer", customer);
		model.addAttribute("healthId", healthId);
		model.addAttribute("insurance", insurances);
		return "seeHealthInsuranceDetails";
	}

	@GetMapping("/healthInsuranceRefund/{insuranceId}")
	public String healthInsuranceRefund(@PathVariable("insuranceId") int insuranceId, Model model,
			RedirectAttributes redirectAttributes) {

		HealthInsurance insurance = healthInsuranceService.getInsuranceById(insuranceId);
		Health health = healthService.getHealthById(insurance.getHealthId());
		Customer customer = customerService.getCustomerById(health.getCustomerId());

		if (insurance.getResult().equals("Canceled") || insurance.getResult().equals("Expired")) {
			redirectAttributes.addFlashAttribute("showAlert", true);
			return "redirect:/seeHealthInsuranceDetails/" + insurance.getHealthId();
		}

		LocalDateTime now = LocalDateTime.now();
		String end_date = now.format(formatter);
		LocalDateTime startingDateTime = LocalDateTime.parse(insurance.getStartDate(), formatter);
		LocalDateTime endDateTime = LocalDateTime.parse(end_date, formatter);

		Duration duration = Duration.between(startingDateTime, endDateTime);
		int daysDiff = (int) duration.toDays(); // daysDiff, poliçeyi ne kadar kullandığı

		insurance.setDaysDiff(daysDiff);
		int remainingDay = insurance.getPeriod() - daysDiff; // remainingDay, poliçenin bitimine ne kadar kaldığı
		double refund = (insurance.getOffer() / insurance.getPeriod()) * remainingDay;
		insurance.setRefund(refund);
		model.addAttribute(refund);
		healthInsuranceService.save(insurance);

		Kdv kdv = kdvService.getProductTypeById(3);

		model.addAttribute(kdv);
		model.addAttribute("insurance", insurance);
		model.addAttribute(health);
		model.addAttribute("customer", customer);
		return "healthInsuranceRefund";
	}

	@PostMapping("/healthResult")
	public String healthResult(@RequestParam("insuranceId") int insuranceId, @RequestParam("result") String result) {

		HealthInsurance insurance = healthInsuranceService.getInsuranceById(insuranceId);

		if (insurance != null) {
			// result değerine göre result sütununu güncelle
			insurance.setResult(result);

			// Poliçeyi iptal ettiyse iptal etme tarihi yazdırılmaktadır
			if (insurance.getResult().equals("Canceled")) {
				LocalDateTime now = LocalDateTime.now();
				String end_date = now.format(formatter);
				insurance.setEndDate(end_date);
			}
			healthInsuranceService.save(insurance);
		}

		return "redirect:/seeHealthInsuranceDetails/" + insurance.getHealthId();
	}
}
