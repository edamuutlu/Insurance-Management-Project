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
import com.insurance.mgmt.entity.Kdv;
import com.insurance.mgmt.service.CustomerService;
import com.insurance.mgmt.service.HealthService;
import com.insurance.mgmt.service.InsuranceService;
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

		CalculateMethods calculateMethods = new CalculateMethods(); // public olan calculate metodunu çağırmak için
																	// util'den nesne oluşturulmaktadır
		Kdv kdv = kdvService.getProductTypeById(3);
		int kdvRate = kdv.getKdvRate();
		double offer = calculateMethods.calculateHealthInsurance(health, kdvRate);
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
				Insurance insurance = insuranceService.getInsuranceByHealthId(health.getHealthId());
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

		for (Health health : healthInfos) {
			if (health.getCustomerId() == customerId) {
				list.add(health);
			}
		}

		model.addAttribute("expiredHealthInsurance", expiredHealthInsurance);
		model.addAttribute("showText", showText);
		return new ModelAndView("healthInfoList", "health", list);
	}

	@GetMapping("/newHealthInsuranceCalculate/{healthId}")
	public String healthInsuranceCalculate(@PathVariable("healthId") int healthId, Model model) {
		Health health = healthService.getHealthById(healthId);
		Insurance insurance = insuranceService.getInsuranceByHealthId(healthId);
		Customer customer = customerService.getCustomerById(health.getCustomerId());

		// Devam eden bir sigorta var mı kontrolü
		List<Insurance> insurances = insuranceService.findByStatusAndResultAndHealthId(1, "Accepted", healthId);
		for (Insurance i : insurances) {
			model.addAttribute("showText", true);
			model.addAttribute(i);
			model.addAttribute(healthId);
			return "seeHealthInsuranceDetails";
		}

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		insurance.setStartDate(now.format(formatter));
		LocalDateTime endDate = now.plusDays(health.getPeriod());
		insurance.setEndDate(endDate.format(formatter));
		insurance.setStatus(1);

		CalculateMethods calculateMethods = new CalculateMethods(); // public olan calculate metodunu çağırmak için
																	// util'den nesne oluşturulmaktadır
		Kdv kdv = kdvService.getProductTypeById(3);
		int kdvRate = kdv.getKdvRate();
		double offer = calculateMethods.calculateHealthInsurance(health, kdvRate);
		insurance.setKdv(kdvRate);
		insurance.setOffer(offer);
		insuranceService.save(insurance);

		model.addAttribute(customer);
		model.addAttribute(insurance);
		model.addAttribute(health);
		return "healthInsuranceCalculate";
	}

	@RequestMapping("/deleteHealthInfo/{id}")
	public String deleteHealth(@PathVariable("id") int healthId, Model model) {
		Health health = healthService.getHealthById(healthId);
		Insurance insurance = insuranceService.getInsuranceByHealthId(health.getHealthId());
		health.setStatus(0);
		insurance.setStatus(0);
		insurance.setResult("Canceled");
		healthService.save(health);
		insuranceService.save(insurance);
		return "redirect:/healthInfoList/" + health.getCustomerId();
	}

	@RequestMapping("/deleteHealthInsurance/{id}")
	public String deleteHomeInsurance(@PathVariable("id") int id, Model model) {
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
		}

		model.addAttribute("healthId", healthId);
		return new ModelAndView("seeHealthInsuranceDetails", "insurance", insurances);
	}

	@GetMapping("/healthInsuranceRefund/{id}")
	public String insuranceRefund(@PathVariable("id") int healthId, Model model,
			RedirectAttributes redirectAttributes) {
		Health health = healthService.getHealthById(healthId);
		Insurance insurance = insuranceService.getInsuranceByHealthId(health.getHealthId());

		if (insurance.getResult().equals("Canceled") || insurance.getResult().equals("Expired")) {
			redirectAttributes.addFlashAttribute("showAlert", true);
			return "redirect:/seeHealthInsuranceDetails/" + healthId;
		}

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String end_date = now.format(formatter);
		LocalDateTime startingDateTime = LocalDateTime.parse(insurance.getStartDate(), formatter);
		LocalDateTime endDateTime = LocalDateTime.parse(end_date, formatter);

		Duration duration = Duration.between(startingDateTime, endDateTime);
		int daysDiff = (int) duration.toDays(); // daysDiff, poliçeyi ne kadar kullandığı

		// System.out.println("Fark: " + daysDiff + " gün");
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

		return "redirect:/healthInfoList/" + insurance.getCustomerId();
	}
}
