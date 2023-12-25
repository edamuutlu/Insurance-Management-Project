package com.insurance.mgmt.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.insurance.mgmt.entity.Car;
import com.insurance.mgmt.entity.CarInsurance;
import com.insurance.mgmt.entity.Company;
import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.entity.Kdv;
import com.insurance.mgmt.entity.address.Province;
import com.insurance.mgmt.service.CarInsuranceService;
import com.insurance.mgmt.service.CarService;
import com.insurance.mgmt.service.CompanyService;
import com.insurance.mgmt.service.CustomerService;
import com.insurance.mgmt.service.HealthInsuranceService;
import com.insurance.mgmt.service.HomeInsuranceService;
import com.insurance.mgmt.service.InsuranceService;
import com.insurance.mgmt.service.KdvService;
import com.insurance.mgmt.service.address.ProvinceService;
import com.insurance.mgmt.util.CalculateMethods;

import jakarta.validation.Valid;

@Controller
public class CarController {
	@Autowired
	CarInsuranceService carInsuranceService;
	
	@Autowired
	HomeInsuranceService homeInsuranceService;
	
	@Autowired
	HealthInsuranceService healthInsuranceService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	CarService carService;
	
	@Autowired
	KdvService kdvService;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	ProvinceService provinceService;
	
	@Autowired
	InsuranceService insuranceService;
		
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	private static final Logger log =  LoggerFactory.getLogger(CarController.class);
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
		//databaseService.disableForeignKeyChecks();
	} 		
	
	@PostMapping("/carRegister")
	public String carRegister(@Valid @ModelAttribute Car car, BindingResult bindingResult, Model model, 
			RedirectAttributes redirectAttributes, @RequestParam(value = "customerId", required = false) int idParam) {		
				
		if(bindingResult.hasErrors()) {
			log.info(">> Car : {}",car.toString());
			model.addAttribute("customerId",car.getCustomerId());
			return "trafficInsuranceForm";
		}		
		model.addAttribute("cars",carService.getAllCars()); 
		
		// Form doldurulurkenki tarih ve sigortanın biteceği tarih hesaplanmaktadır
		CarInsurance insurance = new CarInsurance();
		insurance.setCustomerId(idParam);
		
		LocalDateTime now = LocalDateTime.now();
		insurance.setStartDate(now.format(formatter));
		LocalDateTime endDate = now.plusDays(car.getPeriod());
		insurance.setEndDate(endDate.format(formatter));
		
		Customer customer = customerService.getCustomerById(car.getCustomerId());	
        LocalDate birthDate = LocalDate.parse(customer.getBirth());
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthDate, currentDate).getYears();
        
        Kdv kdv = kdvService.getProductTypeById(1);
		int kdvRate = kdv.getKdvRate();
		double offer = CalculateMethods.calculateCarInsurance(car, age, kdvRate);	
		insurance.setKdv(kdvRate);
		insurance.setOffer(offer);
		insurance.setStatus(1);
		
		List<Car> list = carService.findByStatusAndPlate1AndPlate2AndPlate3(1, car.getPlate1(), car.getPlate2(), car.getPlate3());
		if (!list.isEmpty()) {
		    boolean showPlateAlert = true;	
		    model.addAttribute("showPlateAlert", showPlateAlert);				
		    model.addAttribute("customerId", idParam);
		    return "trafficInsuranceForm";
		}

		car.setCustomerId(idParam);
		car.setStatus(1);
		carService.save(car);
		
		insurance.setInsuranceType("Car");
		insurance.setResult("Canceled"); // Default olarak canceled yazdırılmaktadır
		insurance.setPeriod(car.getPeriod());
		insurance.setCarId(car.getId());
		carInsuranceService.save(insurance);
		
		redirectAttributes.addFlashAttribute("car", car);				
		redirectAttributes.addFlashAttribute("customer",customer);
		model.addAttribute("customerId", idParam);
		redirectAttributes.addFlashAttribute("insurance", insurance);
		return "redirect:/trafficInsuranceCalculate/" + car.getId();	
				
	}
	
	@RequestMapping(path = {"/trafficInsuranceCalculate/{carId}", "/trafficInsuranceCalculate/{carId}/{admin}"}, method = RequestMethod.GET)
	public String trafficInsuranceCalculate(@PathVariable("carId") int carId, @PathVariable(required = false) String admin,Model model) { 
		Car car = carService.getCarId(carId);
		CarInsurance insurance = carInsuranceService.getInsuranceByCarId(carId);
		Customer customer = customerService.getCustomerById(car.getCustomerId());
		
		if(admin == null){
			model.addAttribute("username", customer.getUsername());
		}else if(admin.equals("admin")){
			model.addAttribute("username", "admin");
		}

		List<Company> companyList = companyService.getAllCompany();
		model.addAttribute("companyList", companyList);
		
		model.addAttribute(customer);
		model.addAttribute(insurance);
		model.addAttribute(car);
		return "trafficInsuranceCalculate";
	}
	
	@RequestMapping(path = "/trafficInsuranceForm", method = RequestMethod.GET )
	public String trafficInsuranceForm(@RequestParam(value = "customerId", required = false) int idParam, Model model, @ModelAttribute Car car){
		model.addAttribute("customerId",idParam);
		Customer customer = customerService.getCustomerById(idParam);
		model.addAttribute("customer", customer);
		return "trafficInsuranceForm";
	}
	
	@RequestMapping(path = {"/seeCarInsuranceDetails/{id}", "/seeCarInsuranceDetails/{id}/{admin}"}, method = RequestMethod.GET)
	public String seeCarInsuranceDetails(@PathVariable("id") int carId, @PathVariable(required = false) String admin, Model model,
			RedirectAttributes redirectAttributes) {

		List<CarInsurance> insurances = carInsuranceService.findByStatusAndCarId(1, carId);
		Car car = carService.getCarId(carId);
		Customer customer = customerService.getCustomerById(car.getCustomerId());
		
		for (CarInsurance insurance : insurances) {
			carInsuranceService.save(insurance);
			model.addAttribute("insuranceId", insurance.getInsuranceId());
			model.addAttribute("period", insurance.getPeriod());						
		}

		if (insurances.isEmpty()) {
			model.addAttribute("showAbsent", true);
			model.addAttribute("insurance", insurances);
			model.addAttribute("customer", customer);
			return "seeCarInsuranceDetails";
		}
		
		if(admin == null){
			model.addAttribute("username", customer.getUsername());
		}else if(admin.equals("admin")){
			model.addAttribute("username", "admin");
		}
		
		List<Company> allCompanyList = companyService.getAllCompany();
		System.out.println(allCompanyList);
		model.addAttribute("allCompanyList", allCompanyList);
		
		model.addAttribute("customer", customer);
		model.addAttribute("carId", carId);
		model.addAttribute("insurance", insurances);
		return "seeCarInsuranceDetails";
	}
	
	@PostMapping("/renewCarInsurance/{insuranceId}")
	public String renewCarInsurance(@PathVariable("insuranceId") int insuranceId,
			Model model, RedirectAttributes redirectAttributes) {		
		CarInsurance oldInsurance = carInsuranceService.getInsuranceById(insuranceId);
		Car car = carService.getCarId(oldInsurance.getCarId());
		Customer customer = customerService.getCustomerById(car.getCustomerId());

		// Devam eden bir sigorta var mı kontrolü
		List<CarInsurance> insurances = carInsuranceService.findByStatusAndResultAndCarId(1, "Accepted", oldInsurance.getCarId());
		if (!insurances.isEmpty()) {
			redirectAttributes.addFlashAttribute("showText", true);
			model.addAttribute("insurance", insurances);
			return "redirect:/seeCarInsuranceDetails/" + oldInsurance.getCarId();
		}
	
		//LocalDateTime now = LocalDateTime.now();
		//LocalDateTime endDate = now.plusDays(oldInsurance.getPeriod());

		Kdv kdv = kdvService.getProductTypeById(1);
		int kdvRate = kdv.getKdvRate();
		LocalDate birthDate = LocalDate.parse(customer.getBirth());
		LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthDate, currentDate).getYears();
		double offer = CalculateMethods.calculateCarInsurance(car, age, kdvRate);
	
		int lastInsertedId = insuranceService.saveCarInsurance(oldInsurance, kdvRate, offer);
		CarInsurance newInsurance = carInsuranceService.getInsuranceById(lastInsertedId);
		/*
		CarInsurance newInsurance = new CarInsurance();
		newInsurance.setInsuranceType(oldInsurance.getInsuranceType());
		newInsurance.setCustomerId(oldInsurance.getCustomerId());
		newInsurance.setCarId(oldInsurance.getCarId());
		newInsurance.setStartDate(now.format(formatter));
		newInsurance.setEndDate(endDate.format(formatter));
		newInsurance.setOffer(offer);
		newInsurance.setStatus(oldInsurance.getStatus());
		newInsurance.setResult("Accepted");
		newInsurance.setPeriod(oldInsurance.getPeriod()); 
		newInsurance.setKdv(kdvRate); 
		carInsuranceService.save(newInsurance);
		*/
		
		List<Company> companyList = companyService.getAllCompany();
		model.addAttribute("companyList", companyList);

		model.addAttribute(customer);
		model.addAttribute("insurance", newInsurance);
		model.addAttribute(car);
		return "trafficInsuranceCalculate";
	}
	
	@GetMapping("/carInsuranceRefund/{insuranceId}")
	public String carInsuranceRefund(@PathVariable("insuranceId") int insuranceId, Model model, RedirectAttributes redirectAttributes) {
		
		CarInsurance insurance = carInsuranceService.getInsuranceById(insuranceId);
		Car car = carService.getCarId(insurance.getCarId());
		Customer customer = customerService.getCustomerById(car.getCustomerId());

		if (insurance.getResult().equals("Canceled") || insurance.getResult().equals("Expired")) {
			redirectAttributes.addFlashAttribute("showAlert", true);
			return "redirect:/seeCarInsuranceDetails/" + insurance.getCarId();
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
		carInsuranceService.save(insurance);

		Kdv kdv = kdvService.getProductTypeById(1);
		
		Company company = companyService.findByCompanyId(insurance.getCompanyId());
		model.addAttribute("company", company);
		
		Province province = provinceService.findById(Integer.parseInt(customer.getProvince()));
		model.addAttribute("province", province.getSehiradi());

		model.addAttribute(kdv);
		model.addAttribute("insurance", insurance);
		model.addAttribute(car);
		model.addAttribute("customer", customer);
		model.addAttribute("now", now.format(formatter));
		return "carInsuranceRefund";
	}
	
	@RequestMapping("/deleteCarInsurance/{id}")
	public String deleteCarInsurance(@PathVariable("id") int id, Model model) {
		
		CarInsurance insurance = carInsuranceService.getInsuranceById(id);
		insurance.setResult("Canceled");
		LocalDateTime now = LocalDateTime.now();
		String end_date = now.format(formatter);
		insurance.setEndDate(end_date);
		carInsuranceService.save(insurance);
		return "redirect:/seeCarInsuranceDetails/" + insurance.getCarId();
	}
			
	@RequestMapping("/deleteCar/{id}")
	public String deleteCar(@PathVariable("id") int carId) {
		Car car = carService.getCarId(carId);
		
		List<CarInsurance> insurances = carInsuranceService.findByStatusAndCarId(1, carId);
		if (!(insurances.isEmpty())) {
			for (CarInsurance insurance : insurances) {
				insurance.setStatus(0);
				insurance.setResult("Canceled");
				LocalDateTime now = LocalDateTime.now();
				insurance.setEndDate(now.format(formatter));
				carInsuranceService.save(insurance);
			}
		}
		car.setStatus(0);
		carService.save(car);

		Customer customer = customerService.getCustomerById(car.getCustomerId());
		return "redirect:/userPage/" + customer.getUsername();
	}
	
	@PostMapping("/result")
    public String result(@RequestParam("insuranceId") int insuranceId, @RequestParam("result") String result, @RequestParam("companyId") int companyId) {
		CarInsurance insurance = carInsuranceService.getInsuranceById(insuranceId);

		if (insurance != null) {
			// result değerine göre result sütununu güncelle
			insurance.setCompanyId(companyId);
			insurance.setResult(result);
			if(insurance.getCompanyId() == 0 ) {
				insurance.setOffer(insurance.getOffer() + 0);		
			}else {
				Company company = companyService.findByCompanyId(companyId);
				insurance.setOffer(insurance.getOffer() + company.getServiceFee());
			}

			// Poliçeyi iptal ettiyse iptal etme tarihi yazdırılmaktadır
			if (insurance.getResult().equals("Canceled")) {
				LocalDateTime now = LocalDateTime.now();
				String end_date = now.format(formatter);
				insurance.setEndDate(end_date);
			}
			carInsuranceService.save(insurance);
		}

		return "redirect:/seeCarInsuranceDetails/" + insurance.getCarId();
    }

}
