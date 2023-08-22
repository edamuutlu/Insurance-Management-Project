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
import com.insurance.mgmt.entity.Kdv;
import com.insurance.mgmt.service.CarService;
import com.insurance.mgmt.service.CustomerService;
import com.insurance.mgmt.service.KdvService;
import com.insurance.mgmt.util.CalculateMethods;

import jakarta.validation.Valid;

@Controller
public class CarController {
	
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
		LocalDateTime now = LocalDateTime.now();
		car.setStartDate(now.format(formatter));
		LocalDateTime endDate = now.plusDays(car.getPeriod());
		car.setEndDate(endDate.format(formatter));
		
		Customer customer = customerService.getCustomerById(car.getCustomerId());	
        LocalDate birthDate = LocalDate.parse(customer.getBirth());
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(birthDate, currentDate).getYears();
        
        Kdv kdv = kdvService.getProductTypeById(1);
		int kdvRate = kdv.getKdvRate();
		double offer = CalculateMethods.calculateCarInsurance(car, age, kdvRate);	
		car.setKdv(kdvRate);
		car.setOffer(offer);
		car.setStatus(1);
		
		List<Car> list = carService.findByStatusAndPlate1AndPlate2AndPlate3AndResult(1, car.getPlate1(), car.getPlate2(), car.getPlate3(), "Accepted");
		if (!list.isEmpty()) {
		    boolean showPlateAlert = true;	
		    model.addAttribute("showPlateAlert", showPlateAlert);				
		    model.addAttribute("customerId", idParam);
		    return "trafficInsuranceForm";
		}
		
		car.setResult("Canceled");	// Default olarak canceled yazdırılmaktadır
		carService.save(car);				
		redirectAttributes.addFlashAttribute("car", car);				
		redirectAttributes.addFlashAttribute("customer",customer);	
		return "redirect:/trafficInsuranceCalculate";	
				
	}
	
	@GetMapping("/trafficInsuranceCalculate")
	public String trafficInsuranceCalculate() { 
		return "trafficInsuranceCalculate";
	}
	
	@RequestMapping(path = "/trafficInsuranceForm", method = RequestMethod.GET )
	public String trafficInsuranceForm(@RequestParam(value = "customerId", required = false) int idParam, Model model, @ModelAttribute Car car){
		model.addAttribute("customerId",idParam);
		return "trafficInsuranceForm";
	}
	
	@GetMapping("/carList/{customerId}")
	public ModelAndView carList(@PathVariable("customerId") int customerId, Model model) {	
		ArrayList<Car> expiredCars = new ArrayList<>();
		boolean showText = false;
		
	    // Poliçenin süresinin bitip bitmediğini kontrol etme			
		 List<Car> cars = carService.findByStatusAndCustomerIdAndResult(1, customerId, "Accepted");
		 for(Car car : cars) {
	    		LocalDateTime endDateTime = LocalDateTime.parse(car.getEndDate(), formatter);
	    			    		
	    		LocalDateTime now = LocalDateTime.now();
	    		
	    		Duration duration = Duration.between(now, endDateTime);
	    		int dayCheck = (int) duration.toDays();
	    		
	    		if(dayCheck<0) {
	    			car.setResult("Expired");
	    			expiredCars.add(car);
	    			showText = true;
	    			car.setStatus(0);
	    			carService.save(car);
	    		}
		 }
	    
		List<Car> list = carService.findByStatusAndCustomerId(1, customerId);
	    
	    model.addAttribute("showText", showText);
	    model.addAttribute("expiredCars", expiredCars);
		return new ModelAndView("carList","car",list);
	}	
			
	@RequestMapping("/deleteCar/{id}")
	public String deleteCar(@PathVariable("id") int id) {
		Car car = carService.getCarId(id);
		car.setResult("Canceled");
		LocalDateTime now = LocalDateTime.now();
		String end_date = now.format(formatter);
		car.setEndDate(end_date);
		car.setStatus(0);
		carService.save(car);
		return "redirect:/customerList";
	}
	
	@GetMapping("/trafficInsuranceRefund/{id}")
	public String trafficInsuranceRefund(@PathVariable("id") int id, Model model) {		
		Car car = carService.getCarId(id);
		
		LocalDateTime now = LocalDateTime.now();
		String end_date = now.format(formatter);
		LocalDateTime startingDateTime = LocalDateTime.parse(car.getStartDate(), formatter);
		LocalDateTime endDateTime = LocalDateTime.parse(end_date, formatter);

		Duration duration = Duration.between(startingDateTime, endDateTime);
		int daysDiff = (int) duration.toDays();	// daysDiff, poliçeyi ne kadar kullandığı

		car.setDaysDiff(daysDiff);
		int remainingDay = car.getPeriod() - daysDiff;	// remainingDay, poliçenin bitimine ne kadar kaldığı			
		double refund = (car.getOffer() / car.getPeriod()) * remainingDay;
		car.setRefund(refund);
		model.addAttribute(refund);
		carService.save(car);
		
		Kdv kdv = kdvService.getProductTypeById(1);

		model.addAttribute(kdv);		
		model.addAttribute(car);
		return "trafficInsuranceRefund";
	}
	
	@PostMapping("/result")
    public String result(@RequestParam("carId") int carId, @RequestParam("result") String result) {
        Car car = carService.getCarId(carId);
        
        if (car != null) {
            // result değerine göre result sütununu güncelle
        	car.setResult(result);  
        	
        	//Poliçeyi iptal ettiyse iptal etme tarihi yazdırılmaktadır
        	if(car.getResult().equals("Canceled")) {
        		LocalDateTime now = LocalDateTime.now();
        		String end_date = now.format(formatter);
        		car.setEndDate(end_date);
        	}
        	carService.save(car);
        }

        return "redirect:/customerList";
    }

}
