package com.insurance.mgmt.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
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

import com.insurance.mgmt.entity.Car;
import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.entity.Insurance;
import com.insurance.mgmt.entity.Kdv;
import com.insurance.mgmt.service.CarService;
import com.insurance.mgmt.service.CustomerService;
import com.insurance.mgmt.service.InsuranceService;
import com.insurance.mgmt.service.KdvService;
import com.insurance.mgmt.util.CalculateMethods;

import jakarta.validation.Valid;

@Controller
public class CarController {
	@Autowired
	InsuranceService insuranceService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	CarService carService;
	
	@Autowired
	KdvService kdvService;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	private static final Logger log =  LoggerFactory.getLogger(CarController.class);
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	} 		
	
	@PostMapping("/carRegister")
	public String carRegister(@Valid @ModelAttribute Car car, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam(value = "customerId", required = false) int idParam) {		
				
		if(bindingResult.hasErrors()) {
			log.info(">> Car : {}",car.toString());
			model.addAttribute("customerId",car.getCustomerId());
			return "trafficInsuranceForm";
		}		
		model.addAttribute("cars",carService.getAllCars()); 
		
		// Form doldurulurkenki tarih ve sigortanın biteceği tarih hesaplanmaktadır
		Insurance insurance = new Insurance();
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
		insuranceService.save(insurance);
		
		redirectAttributes.addFlashAttribute("car", car);				
		redirectAttributes.addFlashAttribute("customer",customer);	
		redirectAttributes.addFlashAttribute("insurance", insurance);
		return "redirect:/trafficInsuranceCalculate/" + car.getId();	
				
	}
	
	@GetMapping("/trafficInsuranceCalculate/{carId}")
	public String trafficInsuranceCalculate(@PathVariable("carId") int carId, Model model) { 
		Car car = carService.getCarId(carId);
		Insurance insurance = insuranceService.getInsuranceByCarId(carId);
		Customer customer = customerService.getCustomerById(car.getCustomerId());
		System.out.println(insurance);

		model.addAttribute(customer);
		model.addAttribute(insurance);
		model.addAttribute(car);
		return "trafficInsuranceCalculate";
	}
	
	@RequestMapping(path = "/trafficInsuranceForm", method = RequestMethod.GET )
	public String trafficInsuranceForm(@RequestParam(value = "customerId", required = false) int idParam, Model model, @ModelAttribute Car car){
		model.addAttribute("customerId",idParam);
		return "trafficInsuranceForm";
	}
	
	@GetMapping("/carList/{customerId}")
	public ModelAndView carList(@PathVariable("customerId") int customerId, Model model) {	
						
		List<Car> cars = carService.findByStatusAndCustomerId(1, customerId);
		List<Insurance> insurances = insuranceService.findByStatusAndCustomerIdAndResult(1, customerId, "Accepted");
		ArrayList<Car> expiredCars = new ArrayList<>(); 
		
		// Poliçenin süresinin bitip bitmediğini kontrol etme 
		LocalDateTime now = LocalDateTime.now();
		for(Car car : cars) {
			for (Insurance insurance : insurances) {
		    	LocalDateTime endDateTime = LocalDateTime.parse(insurance.getEndDate(), formatter);    		
		    		
		    	if (now.isAfter(endDateTime)) {		    		
		    		expiredCars.add(car);
		    		model.addAttribute("showText", true);
		    		insurance.setResult("Expired");
		    		insuranceService.save(insurance);
		    	}
			}
		}
	    Customer customer = customerService.getCustomerById(customerId);
	    model.addAttribute("customer",customer);
	    model.addAttribute("expiredCars", expiredCars);
		return new ModelAndView("carList","car",cars);
	}
	
	@GetMapping("/seeCarInsuranceDetails/{id}")
	public String seeCarInsuranceDetails(@PathVariable("id") int carId, Model model,
			RedirectAttributes redirectAttributes) {

		List<Insurance> insurances = insuranceService.findByStatusAndCarId(1, carId);
		for (Insurance insurance : insurances) {
			insuranceService.save(insurance);
			model.addAttribute("insuranceId", insurance.getInsuranceId());
			model.addAttribute("period", insurance.getPeriod());
		}

		if (insurances.isEmpty()) {
			model.addAttribute("showAbsent", true);
			model.addAttribute("insurance", insurances);
			return "seeCarInsuranceDetails";
		}

		model.addAttribute("carId", carId);
		model.addAttribute("insurance", insurances);
		return "seeCarInsuranceDetails";
	}
	
	@PostMapping("/renewCarInsurance/{insuranceId}")
	public String renewHomeInsurance(@PathVariable("insuranceId") int insuranceId, @RequestParam("period") int period,
			Model model) {
		Insurance oldInsurance = insuranceService.getInsuranceById(insuranceId);
		Car car = carService.getCarId(oldInsurance.getCarId());
		Customer customer = customerService.getCustomerById(car.getCustomerId());

		// Devam eden bir sigorta var mı kontrolü
		List<Insurance> insurances = insuranceService.findByStatusAndResultAndCarId(1, "Accepted", oldInsurance.getCarId());
		if (!insurances.isEmpty()) {
			model.addAttribute("showText", true);
			model.addAttribute("insurance", insurances);
			return "seeCarInsuranceDetails";
		}

		Insurance newInsurance = new Insurance();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endDate = now.plusDays(newInsurance.getPeriod());

		Kdv kdv = kdvService.getProductTypeById(2);
		int kdvRate = kdv.getKdvRate();
		LocalDate birthDate = LocalDate.parse(customer.getBirth());
		LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthDate, currentDate).getYears();
		double offer = CalculateMethods.calculateCarInsurance(car, age, kdvRate);
	
		newInsurance.setInsuranceType(oldInsurance.getInsuranceType());
		newInsurance.setCustomerId(oldInsurance.getCustomerId());
		newInsurance.setCarId(oldInsurance.getCarId());
		newInsurance.setStartDate(now.format(formatter));
		newInsurance.setEndDate(endDate.format(formatter));
		newInsurance.setOffer(offer);
		newInsurance.setStatus(oldInsurance.getStatus());
		newInsurance.setResult("Accepted");

		// Kullancıdan alınan yeni periyodu set edilmektedir
		car.setPeriod(period);
		carService.save(car);

		newInsurance.setPeriod(period); 
		insuranceService.save(newInsurance);

		model.addAttribute(customer);
		model.addAttribute("insurance", newInsurance);
		model.addAttribute(car);
		return "trafficInsuranceCalculate";
	}
	
	@GetMapping("/carInsuranceRefund/{insuranceId}")
	public String carInsuranceRefund(@PathVariable("insuranceId") int insuranceId, Model model, RedirectAttributes redirectAttributes) {
		
		Insurance insurance = insuranceService.getInsuranceById(insuranceId);
		Car car = carService.getCarId(insurance.getCarId());

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
		insuranceService.save(insurance);

		Kdv kdv = kdvService.getProductTypeById(2);

		model.addAttribute(kdv);
		model.addAttribute(insurance);
		model.addAttribute(car);
		return "carInsuranceRefund";
	}
	
	@RequestMapping("/deleteCarInsurance/{id}")
	public String deleteCarInsurance(@PathVariable("id") int id, Model model) {
		
		Insurance insurance = insuranceService.getInsuranceById(id);
		insurance.setResult("Canceled");
		LocalDateTime now = LocalDateTime.now();
		String end_date = now.format(formatter);
		insurance.setEndDate(end_date);
		insuranceService.save(insurance);
		return "redirect:/seeCarInsuranceDetails/" + insurance.getCarId();
	}
			
	@RequestMapping("/deleteCar/{id}")
	public String deleteCar(@PathVariable("id") int carId) {
		Car car = carService.getCarId(carId);
		
		List<Insurance> insurances = insuranceService.findByStatusAndCarId(1, carId);
		if (!(insurances.isEmpty())) {
			for (Insurance insurance : insurances) {
				insurance.setStatus(0);
				insurance.setResult("Canceled");
				LocalDateTime now = LocalDateTime.now();
				insurance.setEndDate(now.format(formatter));
				insuranceService.save(insurance);
			}
		}
		car.setStatus(0);
		carService.save(car);

		return "redirect:/carList/" + car.getCustomerId();
	}
	
	@GetMapping("/trafficInsuranceRefund/{insuranceId}")
	public String trafficInsuranceRefund(@PathVariable("insuranceId") int insuranceId, Model model, RedirectAttributes redirectAttributes) {		
		
		Insurance insurance = insuranceService.getInsuranceById(insuranceId);
		Car car = carService.getCarId(insurance.getCarId());

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
		insuranceService.save(insurance);
		
		Kdv kdv = kdvService.getProductTypeById(1);

		model.addAttribute(kdv);
		model.addAttribute(insurance);
		model.addAttribute(car);
		return "trafficInsuranceRefund";
	}
	
	@PostMapping("/result")
    public String result(@RequestParam("insuranceId") int insuranceId, @RequestParam("result") String result) {
		Insurance insurance = insuranceService.getInsuranceById(insuranceId);

		if (insurance != null) {
			// result değerine göre result sütununu güncelle
			insurance.setResult(result);

			// Poliçeyi iptal ettiyse iptal etme tarihi yazdırılmaktadır
			if (insurance.getResult().equals("Canceled")) {
				LocalDateTime now = LocalDateTime.now();
				String end_date = now.format(formatter);
				insurance.setEndDate(end_date);
			}
			insuranceService.save(insurance);
		}

		return "redirect:/seeCarInsuranceDetails/" + insurance.getCarId();
    }

}
