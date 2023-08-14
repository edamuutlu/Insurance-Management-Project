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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.entity.Health;
import com.insurance.mgmt.entity.Insurance;
import com.insurance.mgmt.entity.Jobs;
import com.insurance.mgmt.entity.Kdv;
import com.insurance.mgmt.service.CustomerService;
import com.insurance.mgmt.service.HealthService;
import com.insurance.mgmt.service.InsuranceService;
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
	InsuranceService insuranceService;

	@Autowired
	KdvService kdvService;

	@Autowired
	private JobsService jobsService;

	private static final Logger log = LoggerFactory.getLogger(CarController.class);

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping(path = "/healthInsuranceForm")
	public String getForm(@RequestParam(value = "customerId", required = false) int idParam, Model model) {
		model.addAttribute("customerId", idParam);
		return "healthInsuranceForm";
	}

	@PostMapping("/healthInfoRegister")
	public String register(@Valid @ModelAttribute Health health, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes, @RequestParam(value = "customerId", required = false) int idParam) {

		if (bindingResult.hasErrors()) {
			log.info(">> health : {}", health.toString());
			model.addAttribute("customerId", health.getCustomerId());

			return "healthInsuranceForm";
		}
		model.addAttribute("Healths", healthService.getAllHealths());

		// Form doldurulurkenki tarih ve sigortanın biteceği tarih hesaplanmaktadır
		Insurance insurance = new Insurance();
		insurance.setCustomerId(idParam);
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		insurance.setStartDate(now.format(formatter));
		LocalDateTime endDate = now.plusDays(health.getPeriod());
		insurance.setEndDate(endDate.format(formatter));

		Kdv kdv = kdvService.getProductTypeById(3);
		int kdvRate = kdv.getKdvRate();
		Jobs job = jobsService.getJobById(health.getJob());
		int riskFactor = job.getRiskFactor();
		double offer = CalculateMethods.calculateHealthInsurance(health, kdvRate, riskFactor);
		insurance.setKdv(kdvRate);
		insurance.setOffer(offer);
		insurance.setStatus(1);

		// Aynı kişiye ait sağlık bilgisi kontrolü
		List<Health> filteredList = healthService.findByForWhoAndStatus(health.getForWho(), 1);

		if (!filteredList.isEmpty()) {
			boolean showHealthAlert = true;
			model.addAttribute("showHealthAlert", showHealthAlert);
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
		insuranceService.save(insurance);

		Customer customer = customerService.getCustomerById(health.getCustomerId());

		redirectAttributes.addFlashAttribute("customer", customer);
		redirectAttributes.addFlashAttribute("health", health);
		redirectAttributes.addFlashAttribute("insurance", insurance);

		return "redirect:/healthInsuranceCalculate/" + health.getHealthId();
	}

	@GetMapping("/healthInsuranceCalculate/{healthId}")
	public String healthInsurance(@PathVariable("healthId") int healthId, Model model) {
		Health health = healthService.getHealthById(healthId);
		Insurance insurance = insuranceService.getInsuranceByHealthId(healthId);
		Customer customer = customerService.getCustomerById(health.getCustomerId());
		model.addAttribute("health", health);
		model.addAttribute("insurance", insurance);
		model.addAttribute("customer", customer);
		return "healthInsuranceCalculate";
	}

	@GetMapping("/healthInfoList/{customerId}")
	public ModelAndView getAllHealth(@PathVariable("customerId") int customerId, Model model) {
		List<Health> healthInfos = healthService.findByStatus(1);
		List<Health> list = new ArrayList<>();
		ArrayList<Health> expiredHealthInsurance = new ArrayList<>();
		// Insurance insurance = insuranceService.getInsuranceByCustomerId(customerId);
		boolean showText = false;

		// Poliçenin süresinin bitip bitmediğini kontrol etme
		for (Health health : healthInfos) {
			if (health.getCustomerId() == customerId) {
				List<Insurance> insurances = insuranceService.findByStatusAndCustomerId(1, customerId);
				for (Insurance insurance : insurances) {
					if (insurance.getResult().equals("Accepted")) {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
						LocalDateTime endDateTime = LocalDateTime.parse(insurance.getEndDate(), formatter);

						LocalDateTime now = LocalDateTime.now();

						Duration duration = Duration.between(now, endDateTime);
						int dayCheck = (int) duration.toDays();

						if (dayCheck < 0) {
							expiredHealthInsurance.add(health);
							showText = true;
							insurance.setResult("Expired");
							// insurance.setStatus(0);
							insuranceService.save(insurance);
						}
					}
				}
			}
		}

		for (Health health : healthInfos) {
			if (health.getCustomerId() == customerId) {
				list.add(health);
			}
		}

		model.addAttribute("expiredHealthInsurance", expiredHealthInsurance);
		model.addAttribute("showText", showText);
		return new ModelAndView("healthInfoList", "health", list);
	}

	@GetMapping("/newHealthInsuranceCalculate/{insuranceId}")
	public String healthInsuranceCalculate(@PathVariable("insuranceId") int insuranceId, Model model) {
		Insurance insurance = insuranceService.getInsuranceById(insuranceId);
		Health health = healthService.getHealthById(insurance.getHealthId());
		Customer customer = customerService.getCustomerById(health.getCustomerId());

		// Devam eden bir sigorta var mı kontrolü
		List<Insurance> insurances = insuranceService.findByStatusAndHealthId(1, insurance.getHealthId());
		for (Insurance i : insurances) {
			if ("Accepted".equals(i.getResult())) {
				model.addAttribute("showText", true);
				model.addAttribute("insurance", insurances);
				return "seeHealthInsuranceDetails";
			}
		}

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		insurance.setStartDate(now.format(formatter));
		LocalDateTime endDate = now.plusDays(health.getPeriod());

		Kdv kdv = kdvService.getProductTypeById(3);
		int kdvRate = kdv.getKdvRate();
		Jobs job = jobsService.getJobById(health.getJob());
		int riskFactor = job.getRiskFactor();
		double offer = CalculateMethods.calculateHealthInsurance(health, kdvRate, riskFactor);

		Insurance newInsurance = new Insurance();
		newInsurance.setInsuranceType("Health");
		newInsurance.setCustomerId(insurance.getCustomerId());
		newInsurance.setHealthId(insurance.getHealthId());
		newInsurance.setStartDate(now.format(formatter));
		newInsurance.setEndDate(endDate.format(formatter));
		newInsurance.setPeriod(insurance.getPeriod());
		newInsurance.setKdv(insurance.getKdv());
		newInsurance.setPeriod(insurance.getPeriod());
		newInsurance.setOffer(offer);
		newInsurance.setStatus(1);
		insuranceService.save(newInsurance);

		model.addAttribute(customer);
		model.addAttribute("insurance", newInsurance);
		model.addAttribute(health);
		return "healthInsuranceCalculate";
	}

	@RequestMapping("/deleteHealthInfo/{id}")
	public String deleteHealth(@PathVariable("id") int healthId, Model model) {
		Health health = healthService.getHealthById(healthId);
		List<Insurance> insurances = insuranceService.findByStatusAndHealthId(1, healthId);
		if (!(insurances.isEmpty())) {
			for (Insurance insurance : insurances) {
				insurance.setStatus(0);
				insurance.setResult("Canceled");
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				insurance.setEndDate(now.format(formatter));
				insuranceService.save(insurance);
			}
		}
		health.setStatus(0);
		healthService.save(health);
		return "redirect:/healthInfoList/" + health.getCustomerId();
	}

	@RequestMapping("/deleteHealthInsurance/{id}")
	public String deleteHealthInsurance(@PathVariable("id") int id, Model model) {
		Insurance insurance = insuranceService.getInsuranceById(id);
		insurance.setResult("Canceled");
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String end_date = now.format(formatter);
		insurance.setEndDate(end_date);
		// insurance.setStatus(0);
		insuranceService.save(insurance);
		// insuranceService.deleteById(id); // database den de kalıcı olarak silmek için
		return "redirect:/seeHealthInsuranceDetails/" + insurance.getHealthId();
	}

	@GetMapping("/seeHealthInsuranceDetails/{id}")
	public ModelAndView seeHealthInsuranceDetails(@PathVariable("id") int healthId, Model model) {

		List<Insurance> insurances = insuranceService.findByStatusAndHealthId(1, healthId);
		for (Insurance insurance : insurances) {
			insuranceService.save(insurance);
			model.addAttribute("insuranceId", insurance.getInsuranceId());
		}

		model.addAttribute("healthId", healthId);
		model.addAttribute("insurance", insurances);
		return new ModelAndView("seeHealthInsuranceDetails", "insurance", insurances);
	}

	@GetMapping("/healthInsuranceRefund/{insuranceId}")
	public String insuranceRefund(@PathVariable("insuranceId") int insuranceId, Model model,
			RedirectAttributes redirectAttributes) {
		Insurance insurance = insuranceService.getInsuranceById(insuranceId);
		Health health = healthService.getHealthById(insurance.getHealthId());

		if (insurance.getResult().equals("Canceled") || insurance.getResult().equals("Expired")) {
			redirectAttributes.addFlashAttribute("showAlert", true);
			return "redirect:/seeHealthInsuranceDetails/" + insurance.getHealthId();
		}

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String end_date = now.format(formatter);
		LocalDateTime startingDateTime = LocalDateTime.parse(insurance.getStartDate(), formatter);
		LocalDateTime endDateTime = LocalDateTime.parse(end_date, formatter);

		Duration duration = Duration.between(startingDateTime, endDateTime);
		int daysDiff = (int) duration.toDays(); // daysDiff, poliçeyi ne kadar kullandığı

		insurance.setDaysDiff(daysDiff);
		int remainingDay = insurance.getPeriod() - daysDiff; // remainingDay, poliçenin bitimine ne kadar kaldığı
		double refund = (insurance.getOffer() / insurance.getPeriod()) * remainingDay; // refund, iade edilecek miktar
		insurance.setRefund(refund);
		model.addAttribute(refund);
		insuranceService.save(insurance);

		Kdv kdv = kdvService.getProductTypeById(3);

		model.addAttribute(kdv);
		model.addAttribute(insurance);
		model.addAttribute(health);
		return "healthInsuranceRefund";
	}

	@PostMapping("/healthResult")
	public String result(@RequestParam("insuranceId") int insuranceId, @RequestParam("result") String result) {
		Insurance insurance = insuranceService.getInsuranceById(insuranceId);

		if (insurance != null) {
			// result değerine göre result sütununu güncelle
			insurance.setResult(result);

			// Poliçeyi iptal ettiyse iptal etme tarihi yazdırılmaktadır
			if (insurance.getResult().equals("Canceled")) {
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				String end_date = now.format(formatter);
				insurance.setEndDate(end_date);
			}
			insuranceService.save(insurance);
		}

		return "redirect:/seeHealthInsuranceDetails/" + insurance.getHealthId();
	}
}
