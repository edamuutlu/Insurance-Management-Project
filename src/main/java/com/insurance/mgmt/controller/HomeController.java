package com.insurance.mgmt.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.insurance.mgmt.entity.Home;
import com.insurance.mgmt.entity.Insurance;
import com.insurance.mgmt.repository.IHomeRepository;
import com.insurance.mgmt.repository.IInsuranceRepository;
import com.insurance.mgmt.service.CustomerService;
import com.insurance.mgmt.service.HomeService;
import com.insurance.mgmt.service.InsuranceService;
import com.insurance.mgmt.util.CalculateMethods;

import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	HomeService homeService;

	@Autowired
	IHomeRepository homeRepository;

	@Autowired
	InsuranceService insuranceService;

	@Autowired
	IInsuranceRepository insuranceRepository;

	@Autowired
	CustomerService customerService;

	private static final Logger log = LoggerFactory.getLogger(CarController.class);

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@PostMapping("/homeRegister")
	public String register(@Valid @ModelAttribute Home home, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes, @RequestParam(value = "customerId", required = false) int idParam) {

		if (bindingResult.hasErrors()) {
			log.info(">> home : {}", home.toString());
			model.addAttribute("customerId", home.getCustomerId());
			return "homeInsuranceForm";
		}
		model.addAttribute("homes", homeService.getAllHomes());

		// Form doldurulurkenki tarih ve sigortanın biteceği tarih hesaplanmaktadır
		Insurance insurance = new Insurance();
		insurance.setCustomerId(idParam);
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		insurance.setStartDate(now.format(formatter));
		LocalDateTime endDate = now.plusDays(home.getPeriod());
		insurance.setEndDate(endDate.format(formatter));

		// Bina inşa yılına göre bina yaşı hesaplanmaktadır
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int buildingAge = currentYear - home.getYear();
		home.setBuildingAge(buildingAge);

		CalculateMethods calculateMethods = new CalculateMethods(); // public olan calculate metodunu çağırmak için
																	// util'den nesne oluşturulmaktadır
		int offer = calculateMethods.calculateHomeInsurance(home);
		insurance.setOffer(offer);
		insurance.setStatus(1);

		// Aynı ev adresi kontrolü
		List<Home> filteredList = homeRepository
				.findByProvinceAndDistrictAndNeighbourhoodAndBuildingNumberAndApartmentAndFloorAndStatus(
						home.getProvince(), home.getDistrict(), home.getNeighbourhood(), home.getBuildingNumber(),
						home.getApartment(), home.getFloor(), 1);

		if (!filteredList.isEmpty()) {
			boolean showHomeAlert = true;
			model.addAttribute("showHomeAlert", showHomeAlert);
			model.addAttribute("customerId", idParam);
			model.addAttribute("home", home);
			return "homeInsuranceForm";
		}

		insurance.setInsuranceType("Home");
		insurance.setResult("Canceled"); // Default olarak canceled yazdırılmaktadır
		insurance.setPeriod(home.getPeriod());
		home.setCustomerId(idParam);
		home.setStatus(1);
		homeService.save(home);
		insurance.setHomeId(home.getHomeId());
		insuranceService.save(insurance);

		Customer customer = customerService.getCustomerById(home.getCustomerId());
		redirectAttributes.addFlashAttribute("customer", customer);
		redirectAttributes.addFlashAttribute("home", home);
		redirectAttributes.addFlashAttribute("insurance", insurance);
		return "redirect:/homeInsuranceCalculate/" + home.getHomeId();
	}

	@GetMapping("/homeInsuranceCalculate/{homeId}")
	public String trafficInsurance(@PathVariable("homeId") int homeId, Model model) {
		Home home = homeService.getHomeById(homeId);
		Insurance insurance = insuranceService.getInsuranceByHomeId(homeId);
		Customer customer = customerService.getCustomerById(home.getCustomerId());
		boolean showText = false;
		model.addAttribute("showText", showText);
		model.addAttribute(customer);
		model.addAttribute(insurance);
		model.addAttribute(home);
		return "homeInsuranceCalculate";
	}

	@GetMapping("/newHomeInsuranceCalculate/{homeId}")
	public String homeInsuranceCalculate(@PathVariable("homeId") int homeId, Model model) {
		Home home = homeService.getHomeById(homeId);
		Insurance insurance = insuranceService.getInsuranceByHomeId(homeId);
		Customer customer = customerService.getCustomerById(home.getCustomerId());

		// Devam eden bir sigorta var mı kontrolü
		List<Insurance> insurances = insuranceRepository.findByStatusAndResultAndHomeId(1, "Accepted", homeId);
		for (Insurance i : insurances) {
			model.addAttribute("showText", true);
			model.addAttribute(i);
			model.addAttribute(homeId);
			return "seeHomeInsuranceDetails";
		}

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		insurance.setStartDate(now.format(formatter));
		LocalDateTime endDate = now.plusDays(home.getPeriod());
		insurance.setEndDate(endDate.format(formatter));
		insurance.setStatus(1);

		// Bina inşa yılına göre bina yaşı hesaplanmaktadır
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int buildingAge = currentYear - home.getYear();
		home.setBuildingAge(buildingAge);

		CalculateMethods calculateMethods = new CalculateMethods(); // public olan calculate metodunu çağırmak için
																	// util'den nesne oluşturulmaktadır
		int offer = calculateMethods.calculateHomeInsurance(home);
		insurance.setOffer(offer);
		insuranceService.save(insurance);

		model.addAttribute(customer);
		model.addAttribute(insurance);
		model.addAttribute(home);
		return "homeInsuranceCalculate";
	}

	@RequestMapping(path = "/homeInsuranceForm", method = RequestMethod.GET)
	public String getForm(@RequestParam(value = "customerId", required = false) int idParam, Model model,
			@ModelAttribute Home home) {
		model.addAttribute("customerId", idParam);
		return "homeInsuranceForm";
	}

	@GetMapping("/homeList/{customerId}")
	public ModelAndView getAllHome(@PathVariable("customerId") int customerId, Model model) {
		List<Home> homes = homeRepository.findByStatus(1);
		List<Home> list = new ArrayList<>();
		ArrayList<Home> expiredHomes = new ArrayList<>();
		// Insurance insurance = insuranceService.getInsuranceByCustomerId(customerId);
		boolean showText = false;

		// Poliçenin süresinin bitip bitmediğini kontrol etme
		for (Home home : homes) {
			if (home.getCustomerId() == customerId) {
				Insurance insurance = insuranceService.getInsuranceByHomeId(home.getHomeId());
				if (insurance.getResult().equals("Accepted")) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
					LocalDateTime endDateTime = LocalDateTime.parse(insurance.getEndDate(), formatter);

					LocalDateTime now = LocalDateTime.now();

					Duration duration = Duration.between(now, endDateTime);
					int dayCheck = (int) duration.toDays();

					if (dayCheck < 0) {
						expiredHomes.add(home);
						showText = true;
						insurance.setResult("Expired");
						// insurance.setStatus(0);
						insuranceService.save(insurance);
					}
				}

			}
		}

		for (Home home : homes) {
			if (home.getCustomerId() == customerId) {
				list.add(home);
			}
		}

		model.addAttribute("expiredHomes", expiredHomes);
		model.addAttribute("showText", showText);
		return new ModelAndView("homeList", "home", list);
	}

	@GetMapping("/seeHomeInsuranceDetails/{id}")
	public ModelAndView seeHomeInsuranceDetails(@PathVariable("id") int homeId, Model model,
			RedirectAttributes redirectAttributes) {

		List<Insurance> insurances = insuranceRepository.findByStatusAndHomeId(1, homeId);
		for (Insurance insurance : insurances) {
			insuranceService.save(insurance);
		}

		model.addAttribute("homeId", homeId);
		return new ModelAndView("seeHomeInsuranceDetails", "insurance", insurances);
	}

	@RequestMapping("/deleteHome/{id}")
	public String deleteHome(@PathVariable("id") int id, Model model) {
		Home home = homeService.getHomeById(id);
		Insurance insurance = insuranceService.getInsuranceByHomeId(home.getHomeId());
		home.setStatus(0);
		insurance.setStatus(0);
		insurance.setResult("Canceled");
		homeService.save(home);
		insuranceService.save(insurance);
		return "redirect:/homeList/" + home.getCustomerId();
	}

	@GetMapping("/homeInsuranceRefund/{id}")
	public String insuranceRefund(@PathVariable("id") int id, Model model) {
		Home home = homeService.getHomeById(id);
		Insurance insurance = insuranceService.getInsuranceByHomeId(home.getHomeId());

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
		int refund = (insurance.getOffer() / insurance.getPeriod()) * remainingDay; // refund, iade edilecek miktar
		insurance.setRefund(refund);
		model.addAttribute(refund);
		insuranceService.save(insurance);

		model.addAttribute(insurance);
		model.addAttribute(home);
		return "homeInsuranceRefund";
	}

	@RequestMapping("/deleteHomeInsurance/{id}")
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
		return "redirect:/seeHomeInsuranceDetails/" + insurance.getHomeId();
	}

	@PostMapping("/homeResult")
	public String result(@RequestParam("insuranceId") int insuranceId, @RequestParam("result") String result) {
		// carId'ye göre veritabanında aracı bulun
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

		return "redirect:/seeHomeInsuranceDetails/" + insurance.getHomeId();
	}

}
