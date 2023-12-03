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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.entity.Home;
import com.insurance.mgmt.entity.HomeInsurance;
import com.insurance.mgmt.entity.Kdv;
import com.insurance.mgmt.entity.address.District;
import com.insurance.mgmt.entity.address.Neighbourhood;
import com.insurance.mgmt.entity.address.Province;
import com.insurance.mgmt.service.CarInsuranceService;
import com.insurance.mgmt.service.CustomerService;
import com.insurance.mgmt.service.HealthInsuranceService;
import com.insurance.mgmt.service.HomeInsuranceService;
import com.insurance.mgmt.service.HomeService;
import com.insurance.mgmt.service.KdvService;
import com.insurance.mgmt.service.address.DistrictService;
import com.insurance.mgmt.service.address.NeighbourhoodService;
import com.insurance.mgmt.service.address.ProvinceService;
import com.insurance.mgmt.util.CalculateMethods;

import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	HomeService homeService;

	@Autowired
	CarInsuranceService carInsuranceService;
	
	@Autowired
	HomeInsuranceService homeInsuranceService;
	
	@Autowired
	HealthInsuranceService healthInsuranceService;

	@Autowired
	CustomerService customerService;

	@Autowired
	KdvService kdvService;

	@Autowired
	ProvinceService provinceService;

	@Autowired
	DistrictService districtService;

	@Autowired
	NeighbourhoodService neighbourhoodService;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	private static final Logger log = LoggerFactory.getLogger(HomeController.class);

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@PostMapping("/homeRegister")
	public String homeRegister(@Valid @ModelAttribute Home home, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes, @RequestParam(value = "customerId", required = false) int idParam) {

		List<Province> getAllProvinces = provinceService.listAll();
		List<District> getAllDistricts = districtService.listAll();
		List<Neighbourhood> getAllNeighbourhoods = neighbourhoodService.listAll();

		if (bindingResult.hasErrors()) {
			log.info(">> home : {}", home.toString());
			model.addAttribute("customerId", home.getCustomerId());

			model.addAttribute("getAllProvinces", getAllProvinces);
			model.addAttribute("getAllDistricts", getAllDistricts);
			model.addAttribute("getAllNeighbourhoods", getAllNeighbourhoods);

			return "homeInsuranceForm";
		}
		model.addAttribute("homes", homeService.getAllHomes());

		// Form doldurulurkenki tarih ve sigortanın biteceği tarih hesaplanmaktadır
		HomeInsurance insurance = new HomeInsurance();
		insurance.setCustomerId(idParam);

		LocalDateTime now = LocalDateTime.now();
		insurance.setStartDate(now.format(formatter));
		LocalDateTime endDate = now.plusDays(home.getPeriod());
		insurance.setEndDate(endDate.format(formatter));

		// Bina inşa yılına göre bina yaşı hesaplanmaktadır
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int buildingAge = currentYear - home.getYear();
		home.setBuildingAge(buildingAge);

		Kdv kdv = kdvService.getProductTypeById(2);
		int kdvRate = kdv.getKdvRate();
		insurance.setKdv(kdvRate);

		double offer = CalculateMethods.calculateHomeInsurance(home, kdvRate);
		insurance.setOffer(offer);

		insurance.setStatus(1);

		// Aynı ev adresi kontrolü
		List<Home> filteredList = homeService
				.findByProvinceAndDistrictAndNeighbourhoodAndBuildingNumberAndApartmentAndFloorAndStatus(
						home.getProvince(), home.getDistrict(), home.getNeighbourhood(), home.getBuildingNumber(),
						home.getApartment(), home.getFloor(), 1);

		if (!filteredList.isEmpty()) {
			model.addAttribute("showHomeAlert", true);
			model.addAttribute("customerId", idParam);
			model.addAttribute("home", home);

			model.addAttribute("getAllProvinces", getAllProvinces);
			model.addAttribute("getAllDistricts", getAllDistricts);
			model.addAttribute("getAllNeighbourhoods", getAllNeighbourhoods);
			return "homeInsuranceForm";
		}

		insurance.setInsuranceType("Home");
		insurance.setResult("Canceled"); // Default olarak canceled yazdırılmaktadır
		insurance.setPeriod(home.getPeriod());

		home.setCustomerId(idParam);
		home.setStatus(1);
		homeService.save(home);
		
		insurance.setHomeId(home.getHomeId());
		homeInsuranceService.save(insurance);

		Customer customer = customerService.getCustomerById(home.getCustomerId());
		redirectAttributes.addFlashAttribute("customer", customer);
		redirectAttributes.addFlashAttribute("home", home);
		redirectAttributes.addFlashAttribute("insurance", insurance);
		return "redirect:/homeInsuranceCalculate/" + home.getHomeId();
	}

	@GetMapping("/homeInsuranceCalculate/{homeId}")
	public String homeInsuranceCalculate(@PathVariable("homeId") int homeId, Model model) {

		Home home = homeService.getHomeById(homeId);
		HomeInsurance insurance = homeInsuranceService.getInsuranceByHomeId(homeId);
		Customer customer = customerService.getCustomerById(home.getCustomerId());

		model.addAttribute(customer);
		model.addAttribute("insurance", insurance);
		model.addAttribute(home);
		return "homeInsuranceCalculate";
	}

	@GetMapping(path = "/homeInsuranceForm")
	public String homeInsuranceForm(@RequestParam(value = "customerId", required = false) int idParam, Model model,
			@ModelAttribute Home home) {

		List<Province> getAllProvinces = provinceService.listAll();
		List<District> getAllDistricts = districtService.listAll();
		List<Neighbourhood> getAllNeighbourhoods = neighbourhoodService.listAll();

		model.addAttribute("getAllProvinces", getAllProvinces);
		model.addAttribute("getAllDistricts", getAllDistricts);
		model.addAttribute("getAllNeighbourhoods", getAllNeighbourhoods);
		model.addAttribute("customerId", idParam);
		return "homeInsuranceForm";
	}

	@GetMapping("/homeList/{customerId}")
	public ModelAndView homeList(@PathVariable("customerId") int customerId, Model model) {

		List<Home> homes = homeService.findByStatusAndCustomerId(1, customerId);
		List<HomeInsurance> insurances = homeInsuranceService.findByStatusAndCustomerIdAndResult(1, customerId, "Accepted");
		ArrayList<Home> expiredHomes = new ArrayList<>();

		// Poliçenin süresinin bitip bitmediğini kontrol etme
		LocalDateTime now = LocalDateTime.now();
		for (Home home : homes) {
			for (HomeInsurance insurance : insurances) {
				LocalDateTime endDateTime = LocalDateTime.parse(insurance.getEndDate(), formatter);

				if (now.isAfter(endDateTime)) {
					expiredHomes.add(home);
					model.addAttribute("showText", true);
					insurance.setResult("Expired");
					homeInsuranceService.save(insurance);
				}
			}
		}

		model.addAttribute("expiredHomes", expiredHomes);
		return new ModelAndView("homeList", "home", homes);
	}

	@GetMapping("/seeHomeInsuranceDetails/{id}")
	public String seeHomeInsuranceDetails(@PathVariable("id") int homeId, Model model,
			RedirectAttributes redirectAttributes) {

		List<HomeInsurance> insurances = homeInsuranceService.findByStatusAndHomeId(1, homeId);
		Home home = homeService.getHomeById(homeId);
		Customer customer = customerService.getCustomerById(home.getCustomerId());
		
		for (HomeInsurance insurance : insurances) {
			homeInsuranceService.save(insurance);
			model.addAttribute("insuranceId", insurance.getInsuranceId());
			model.addAttribute("period", insurance.getPeriod());
		}

		if (insurances.isEmpty()) {
			model.addAttribute("showAbsent", true);
			model.addAttribute("insurance", insurances);
			model.addAttribute("customer", customer);
			return "seeHomeInsuranceDetails";
		}

		model.addAttribute("customer", customer);
		model.addAttribute("homeId", homeId);
		model.addAttribute("insurance", insurances);
		return "seeHomeInsuranceDetails";
	}

	@PostMapping("/renewHomeInsurance/{insuranceId}")
	public String renewHomeInsurance(@PathVariable("insuranceId") int insuranceId,
			Model model, RedirectAttributes redirectAttributes) {
		HomeInsurance oldInsurance = homeInsuranceService.getInsuranceById(insuranceId);
		Home home = homeService.getHomeById(oldInsurance.getHomeId());
		Customer customer = customerService.getCustomerById(home.getCustomerId());

		// Devam eden bir sigorta var mı kontrolü
		List<HomeInsurance> insurances = homeInsuranceService.findByStatusAndResultAndHomeId(1, "Accepted", oldInsurance.getHomeId());
		if (!insurances.isEmpty()) {
			redirectAttributes.addFlashAttribute("showText", true);
			model.addAttribute("insurance", insurances);
			return "redirect:/seeHomeInsuranceDetails/" + oldInsurance.getHomeId();
		}

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endDate = now.plusDays(oldInsurance.getPeriod());

		// Bina inşa yılına göre bina yaşı hesaplanmaktadır
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int buildingAge = currentYear - home.getYear();
		home.setBuildingAge(buildingAge);

		Kdv kdv = kdvService.getProductTypeById(2);
		int kdvRate = kdv.getKdvRate();
		double offer = CalculateMethods.calculateHomeInsurance(home, kdvRate);

		HomeInsurance newInsurance = new HomeInsurance();
		newInsurance.setInsuranceType(oldInsurance.getInsuranceType());
		newInsurance.setCustomerId(oldInsurance.getCustomerId());
		newInsurance.setHomeId(oldInsurance.getHomeId());
		newInsurance.setStartDate(now.format(formatter));
		newInsurance.setEndDate(endDate.format(formatter));
		newInsurance.setKdv(oldInsurance.getKdv());
		newInsurance.setOffer(offer);
		newInsurance.setStatus(oldInsurance.getStatus());
		newInsurance.setPeriod(oldInsurance.getPeriod()); 
		homeInsuranceService.save(newInsurance);

		model.addAttribute(customer);
		model.addAttribute("insurance", newInsurance);
		model.addAttribute(home);
		return "homeInsuranceCalculate";
	}

	@RequestMapping("/deleteHome/{id}")
	public String deleteHome(@PathVariable("id") int homeId, Model model) {
		
		Home home = homeService.getHomeById(homeId);
		List<HomeInsurance> insurances = homeInsuranceService.findByStatusAndHomeId(1, homeId);
		if (!(insurances.isEmpty())) {
			for (HomeInsurance insurance : insurances) {
				insurance.setStatus(0);
				insurance.setResult("Canceled");
				LocalDateTime now = LocalDateTime.now();
				insurance.setEndDate(now.format(formatter));
				homeInsuranceService.save(insurance);
			}
		}
		home.setStatus(0);
		homeService.save(home);

		Customer customer = customerService.getCustomerById(home.getCustomerId());
		return "redirect:/userPage/" + customer.getUsername();
	}

	@GetMapping("/homeInsuranceRefund/{insuranceId}")
	public String homeInsuranceRefund(@PathVariable("insuranceId") int insuranceId, Model model, RedirectAttributes redirectAttributes) {
		
		HomeInsurance insurance = homeInsuranceService.getInsuranceById(insuranceId);
		Home home = homeService.getHomeById(insurance.getHomeId());

		if (insurance.getResult().equals("Canceled") || insurance.getResult().equals("Expired")) {
			redirectAttributes.addFlashAttribute("showAlert", true);
			return "redirect:/seeHomeInsuranceDetails/" + insurance.getHomeId();
		}

		LocalDateTime now = LocalDateTime.now();
		String end_date = now.format(formatter);
		LocalDateTime startingDateTime = LocalDateTime.parse(insurance.getStartDate(), formatter);
		LocalDateTime endDateTime = LocalDateTime.parse(end_date, formatter);

		Duration duration = Duration.between(startingDateTime, endDateTime);
		int daysDiff = (int) duration.toDays(); 

		insurance.setDaysDiff(daysDiff);
		int remainingDay = insurance.getPeriod() - daysDiff; 
		double refund = (insurance.getOffer() / insurance.getPeriod()) * remainingDay; 
		insurance.setRefund(refund);
		model.addAttribute(refund);
		homeInsuranceService.save(insurance);

		Kdv kdv = kdvService.getProductTypeById(2);

		model.addAttribute(kdv);
		model.addAttribute("insurance", insurance);
		model.addAttribute(home);
		return "homeInsuranceRefund";
	}

	@RequestMapping("/deleteHomeInsurance/{id}")
	public String deleteHomeInsurance(@PathVariable("id") int id, Model model) {
		
		HomeInsurance insurance = homeInsuranceService.getInsuranceById(id);
		insurance.setResult("Canceled");
		LocalDateTime now = LocalDateTime.now();
		String end_date = now.format(formatter);
		insurance.setEndDate(end_date);
		homeInsuranceService.save(insurance);
		return "redirect:/seeHomeInsuranceDetails/" + insurance.getHomeId();
	}

	@PostMapping("/homeResult")
	public String homeResult(@RequestParam("insuranceId") int insuranceId, @RequestParam("result") String result) {
		
		HomeInsurance insurance = homeInsuranceService.getInsuranceById(insuranceId);

		if (insurance != null) {
			// result değerine göre result sütununu güncelle
			insurance.setResult(result);

			// Poliçeyi iptal ettiyse iptal etme tarihi yazdırılmaktadır
			if (insurance.getResult().equals("Canceled")) {
				LocalDateTime now = LocalDateTime.now();
				String end_date = now.format(formatter);
				insurance.setEndDate(end_date);
			}
			homeInsuranceService.save(insurance);
		}

		return "redirect:/seeHomeInsuranceDetails/" + insurance.getHomeId();
	}
	
}
