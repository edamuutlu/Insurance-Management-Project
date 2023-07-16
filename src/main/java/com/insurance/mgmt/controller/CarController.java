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
import com.insurance.mgmt.service.CarService;
import com.insurance.mgmt.service.CustomerService;

import jakarta.validation.Valid;

@Controller
public class CarController {
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	CarService carService;
	
	private static final Logger log =  LoggerFactory.getLogger(CarController.class);
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	} 
	
	public int calculate(Car car) {
		// Sigorta teklifi için hesaplamalar
				int offer=500; // Sabit bir başlangıç teklifi-primi
				
				Customer customer = customerService.getCustomerById(car.getCustomer_id());				
				// String'i LocalDate nesnesine dönüştürme
		        LocalDate birthDate = LocalDate.parse(customer.getBirth());
		        // Bugünkü tarihi al
		        LocalDate currentDate = LocalDate.now();
		        // Yaşı hesapla
		        int age = Period.between(birthDate, currentDate).getYears();
		        if(age>18 && age<=25) {
		        	offer+= age * 4;
		        }else if (age>25 && age<=60) {
		        	offer+= age * 3;
				}else {
					offer+= age * 5;
				}
				
				switch (car.getType()) {	// Araç tipi için ek prim
				    case "Car":
				        offer += 4000;
				        break;
				    case "Truck":
				        offer += 5000;
				        break;
				    case "Van":
				        offer += 3000;
				        break;
				    default:
						break;
				}
			
				switch (car.getPurpose()) {	// Kullanım amacı için ek prim
				    case "Private":
				        offer += 4000;
				        break;
				    case "Commercial":
				        offer += 5000;
				        break;
				    default:
						break;
				}
				
				switch (car.getBrand()) {	// Araç modeli için ek prim
					case "Audi":
					case "BMW":
					case "Lamborghini":
					case "Mercedes":
					case "Volvo":
						offer+=3000;
						break;
					case "Citroen":
					case "Dacia":
					case "Hyundai":
					case "Kia":
					case "Jeep":
						offer+=2000;
						break;
					case "Fiat":
					case "Nissan":
					case "Opel":
					case "Renault":
					case "Skoda":
						offer+=1000;
						break;
					default:
						break;
				}
				
				// Sigortanın kapsadığı süreye bağlı olarak ek prim
				offer += car.getPeriod() * 20; 
				
				switch (car.getFuel_type()) {	// Araç yakıt tipi için ek prim
					case "Petrol":
						offer+=3000;
						break;
					case "Diesel":
						offer+=2000;
						break;
					case "LPG":
						offer+=2000;
						break;
					case "Electric":
						offer+=2000;
						break;
					default:
						break;
				}
				
				if(car.getEngine_size()<2) {	// Motor hacmine göre ek prim
					offer+= (car.getEngine_size()) * 100;
				}else if(car.getEngine_size()>2) {
					offer+=(car.getEngine_size()) * 200;
				}
				
				switch (car.getLicense_plate1()) {	// Aracın kayıtlı olduğu ilin trafik yoğunluğuna göre ek prim
				case 06:
					offer+=4000;
					break;
				case 16:
					offer+=3000;
					break;
				case 34:
					offer+=5000;
					break;
			    default:
					System.out.println("Geçerli olmayan il");
					break;
				}
				
				if(car.getSeat_capacity()<=2) {	// Aracın koltuk kapasitesine göre ek prim
					offer+= (car.getSeat_capacity()) * 20;
				}else if(car.getSeat_capacity()>2 && car.getSeat_capacity()<=5) {
					offer+= (car.getSeat_capacity()) * 30;
				}else { 
					offer+= (car.getSeat_capacity()) * 40;
				}
				return offer;
	}
	
	@PostMapping("/carRegister")
	public String register(@Valid @ModelAttribute Car car, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam(value = "customer_id", required = false) int idParam) {		
				
		if(bindingResult.hasErrors()) {
			log.info(">> Car : {}",car.toString());
			model.addAttribute("customer_id",car.getCustomer_id());
			return "trafficInsuranceForm";
		}		
		model.addAttribute("cars",carService.getAllCars()); 
		
		// Form doldurulurkenki tarih ve saat bilgisi hesaplanmaktadır
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		car.setStarting_date(now.format(formatter));
		
		int offer = calculate(car);	
		car.setOffer(offer);
		car.setStatus(1);	
		
		// Aynı plaka kontrolü
		boolean showPlateAlert = false;
		
		List<Car> list = carService.getAllCars();
		for (Car c : list) {
			if(c.getStatus() == 1 && c.getLicense_plate1() == car.getLicense_plate1() && c.getLicense_plate2().equals(car.getLicense_plate2()) 
					&& c.getLicense_plate3() == car.getLicense_plate3() && c.getResult().equals("Accepted")) {
				showPlateAlert = true;	
				model.addAttribute("showPlateAlert",showPlateAlert);				
				model.addAttribute("customer_id",idParam);
				return "trafficInsuranceForm";
			}			
		}
		car.setResult("Canceled");	// Default olarak canceled yazdırılmaktadır
		carService.save(car);				
		redirectAttributes.addFlashAttribute("car", car);			
		Customer customer = customerService.getCustomerById(car.getCustomer_id());				
		redirectAttributes.addFlashAttribute("customer",customer);	
		return "redirect:/trafficInsuranceCalculate";	
				
	}
	
	@GetMapping("/trafficInsuranceCalculate")
	public String trafficInsurance() { 
		return "trafficInsuranceCalculate";
	}
	
	
	@RequestMapping(path = "/trafficInsuranceForm", method = RequestMethod.GET )
	public String getForm(@RequestParam(value = "customer_id", required = false) int idParam, Model model, @ModelAttribute Car car, RedirectAttributes redirectAttributes){
		model.addAttribute("customer_id",idParam);
		return "trafficInsuranceForm";
	}
	
	@GetMapping("/carList/{customer_id}")
	public ModelAndView getAllCar(@PathVariable("customer_id") int customer_id, Model model) {	
		List<Car> cars = carService.getAllCars();
		List<Car> list = new ArrayList<>();
		ArrayList<Car> expiredCars = new ArrayList<>();
		boolean showText = false;
		
	    // Poliçenin süresinin bitip bitmediğini kontrol etme
	    for(Car car : cars) {
	    	if (car.getCustomer_id() == customer_id && car.getStatus() == 1 && car.getResult().equals("Accepted")) {
	    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	    		LocalDateTime startingDateTime = LocalDateTime.parse(car.getStarting_date(), formatter);
	    		LocalDateTime endDateTime = startingDateTime.plusDays(2);
	    			    		
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
	    }
	    	    
	    for (Car car : cars) {
	        if (car.getCustomer_id() == customer_id && car.getStatus() == 1) {
	        	list.add(car);	        	
	        }
	    }
	    //System.out.println(expiredCars.get(0).getBrand());
	    model.addAttribute("showText", showText);
	    model.addAttribute("expiredCars", expiredCars);
		return new ModelAndView("carList","car",list);
	}	
			
	@RequestMapping("/deleteCar/{id}")
	public String deleteCar(@PathVariable("id") int id) {
		Car car = carService.getCarId(id);
		car.setStatus(0);
		carService.save(car);
		//carService.deleteById(id);
		return "redirect:/customerList";
	}
	
	@GetMapping("/insuranceRefund/{id}")
	public String insuranceRefund(@PathVariable("id") int id, Model model) {		
		Car car = carService.getCarId(id);
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String end_date = now.format(formatter);
		LocalDateTime startingDateTime = LocalDateTime.parse(car.getStarting_date(), formatter);
		LocalDateTime endDateTime = LocalDateTime.parse(end_date, formatter);

		Duration duration = Duration.between(startingDateTime, endDateTime);
		int daysDiff = (int) duration.toDays();	// daysDiff, poliçeyi ne kadar kullandığı

		//System.out.println("Fark: " + daysDiff + " gün");
		car.setDays_diff(daysDiff);
		int remainingDay = car.getPeriod() - daysDiff;	// remainingDay, poliçenin bitimine ne kadar kaldığı			
		int refund = (car.getOffer() / car.getPeriod()) * remainingDay;	// refund, iade edilecek miktar
		car.setRefund(refund);
		//System.out.println("İade: "+ refund);
		model.addAttribute(refund);
		carService.save(car);
		
		model.addAttribute(car);
		return "insuranceRefund";
	}
	
	@PostMapping("/result")
    public String result(@RequestParam("carId") int carId,
                            @RequestParam("result") String result) {
        // carId'ye göre veritabanında aracı bulun
        Car car = carService.getCarId(carId);
        
        if (car != null) {
            // result değerine göre result sütununu güncelle
        	car.setResult(result);  
        	carService.save(car);
        }
        
        // Sayfayı yeniden yönlendir veya başka bir işlem yap
        return "redirect:/customerList";
    }

}
