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
import com.insurance.mgmt.entity.Insurance;
import com.insurance.mgmt.entity.Kdv;
import com.insurance.mgmt.entity.address.District;
import com.insurance.mgmt.entity.address.Neighbourhood;
import com.insurance.mgmt.entity.address.Province;
import com.insurance.mgmt.service.CustomerService;
import com.insurance.mgmt.service.HomeService;
import com.insurance.mgmt.service.InsuranceService;
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
	InsuranceService insuranceService;

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

	private static final Logger log = LoggerFactory.getLogger(CarController.class);

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@PostMapping("/homeRegister")
	public String register(@Valid @ModelAttribute Home home, BindingResult bindingResult, Model model,
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

		Kdv kdv = kdvService.getProductTypeById(2);
		int kdvRate = kdv.getKdvRate();
		double offer = CalculateMethods.calculateHomeInsurance(home, kdvRate);
		insurance.setKdv(kdvRate);
		
		// offerkdv ?
		
		insurance.setOffer(offer);
		insurance.setStatus(1);

		// Aynı ev adresi kontrolü
		List<Home> filteredList = homeService
				.findByProvinceAndDistrictAndNeighbourhoodAndBuildingNumberAndApartmentAndFloorAndStatus(
						home.getProvince(), home.getDistrict(), home.getNeighbourhood(), home.getBuildingNumber(),
						home.getApartment(), home.getFloor(), 1);

		if (!filteredList.isEmpty()) {
			boolean showHomeAlert = true;
			model.addAttribute("showHomeAlert", showHomeAlert);
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
		insuranceService.save(insurance);

		Customer customer = customerService.getCustomerById(home.getCustomerId());
		redirectAttributes.addFlashAttribute("customer", customer);
		redirectAttributes.addFlashAttribute("home", home);
		redirectAttributes.addFlashAttribute("insurance", insurance);
		return "redirect:/homeInsuranceCalculate/" + home.getHomeId();
	}

	@GetMapping("/homeInsuranceCalculate/{homeId}")
	public String homeInsuranceCalculate(@PathVariable("homeId") int homeId, Model model) {
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

	@GetMapping("/newHomeInsuranceCalculate/{insuranceId}")
	public String newHomeInsuranceCalculate(@PathVariable("insuranceId") int insuranceId, Model model) {			
		Insurance insurance = insuranceService.getInsuranceById(insuranceId);
		Home home = homeService.getHomeById(insurance.getHomeId());
		Customer customer = customerService.getCustomerById(home.getCustomerId());

		// Devam eden bir sigorta var mı kontrolü
		List<Insurance> insurances = insuranceService.findByStatusAndHomeId(1, insurance.getHomeId());
		
		for (Insurance i : insurances) {
			if ("Accepted".equals(i.getResult())) {
				model.addAttribute("showText", true);
				model.addAttribute("insurance", insurances);
				return "seeHomeInsuranceDetails";
		    }			
		}

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime endDate = now.plusDays(home.getPeriod());

		// Bina inşa yılına göre bina yaşı hesaplanmaktadır
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int buildingAge = currentYear - home.getYear();
		home.setBuildingAge(buildingAge);

		Kdv kdv = kdvService.getProductTypeById(2);
		int kdvRate = kdv.getKdvRate();
		double offer = CalculateMethods.calculateHomeInsurance(home, kdvRate);

		Insurance newInsurance = new Insurance();
		newInsurance.setInsuranceType("Home");
		newInsurance.setCustomerId(insurance.getCustomerId());
		newInsurance.setHomeId(insurance.getHomeId());
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
		model.addAttribute(home);
		return "homeInsuranceCalculate";
	}

	@GetMapping(path = "/homeInsuranceForm")
	public String getForm(@RequestParam(value = "customerId", required = false) int idParam, Model model,
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
	public ModelAndView getAllHome(@PathVariable("customerId") int customerId, Model model) {
		List<Home> homes = homeService.findByStatus(1);
		List<Home> list = new ArrayList<>();
		ArrayList<Home> expiredHomes = new ArrayList<>();
		// Insurance insurance = insuranceService.getInsuranceByCustomerId(customerId);
		boolean showText = false;

		// Poliçenin süresinin bitip bitmediğini kontrol etme
		for (Home home : homes) {
			if (home.getCustomerId() == customerId) {
				List<Insurance> insurances = insuranceService.findByStatusAndCustomerId(1, customerId);
				for (Insurance insurance : insurances) {
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
	public String seeHomeInsuranceDetails(@PathVariable("id") int homeId, Model model, RedirectAttributes redirectAttributes) {
		List<Insurance> insurances = insuranceService.findByStatusAndHomeId(1, homeId);
//		if(insurances.isEmpty()) {
//			List<Customer> customerList = customerService.findByStatus(1);
//			
//			Kdv carKdv = kdvService.getProductTypeById(1);
//			Kdv homeKdv = kdvService.getProductTypeById(2);
//			Kdv healthKdv = kdvService.getProductTypeById(3);
//			model.addAttribute("carKdv", carKdv);
//			model.addAttribute("homeKdv", homeKdv);
//			model.addAttribute("healthKdv", healthKdv);
//			model.addAttribute("customer",  customerList);
//			
//			return "customerList";
//		}
		for (Insurance insurance : insurances) {
			insuranceService.save(insurance); 
			model.addAttribute("insuranceId", insurance.getInsuranceId());
		}

		model.addAttribute("homeId", homeId);
		model.addAttribute("insurance", insurances);
		return "seeHomeInsuranceDetails";
		//return new ModelAndView("seeHomeInsuranceDetails", "insurance", insurances);
	}

	@RequestMapping("/deleteHome/{id}")
	public String deleteHome(@PathVariable("id") int homeId, Model model) {
		Home home = homeService.getHomeById(homeId);
		List<Insurance> insurances = insuranceService.findByStatusAndHomeId(1, homeId);	
		if(!(insurances.isEmpty())) {
			for (Insurance insurance : insurances) {
				insurance.setStatus(0);
				insurance.setResult("Canceled");
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				insurance.setEndDate(now.format(formatter));
				insuranceService.save(insurance);
			}
		}		
		home.setStatus(0);
		homeService.save(home);
		
		return "redirect:/homeList/" + home.getCustomerId();
	}

	@GetMapping("/homeInsuranceRefund/{insuranceId}")
	public String insuranceRefund(@PathVariable("insuranceId") int insuranceId, Model model, RedirectAttributes redirectAttributes) {		
		Insurance insurance = insuranceService.getInsuranceById(insuranceId);
		Home home = homeService.getHomeById(insurance.getHomeId());

		if (insurance.getResult().equals("Canceled") || insurance.getResult().equals("Expired")) {
			redirectAttributes.addFlashAttribute("showAlert", true);
			return "redirect:/seeHomeInsuranceDetails/" + insurance.getHomeId();
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

		Kdv kdv = kdvService.getProductTypeById(2);

		model.addAttribute(kdv);
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
