package com.insurance.mgmt.controller;

import java.time.LocalDate;
import java.time.Period;
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
	public String register(@Valid @ModelAttribute Car car, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {		
				
		if(bindingResult.hasErrors()) {
			log.info(">> Car : {}",car.toString());
			model.addAttribute("customer_id",car.getCustomer_id());
			return "trafficInsuranceForm";
		}		
		model.addAttribute("cars",carService.getAllCars()); 
			
		int offer = calculate(car);	
		car.setOffer(offer);
		car.setStatus(1);
		carService.save(car);
		
		redirectAttributes.addFlashAttribute("car", car);
		
		Customer customer = customerService.getCustomerById(car.getCustomer_id());
		
		redirectAttributes.addFlashAttribute("customer",customer);		
		
		return "redirect:/trafficInsuranceCalculate";
	}
	
	@GetMapping("/trafficInsuranceCalculate")
	public String trafficInsurance(Model model) { // RedirectAttributes redirectAttributes
		
		return "trafficInsuranceCalculate";
	}
	
	
	@RequestMapping(path = "/trafficInsuranceForm", method = RequestMethod.GET )
	public String getMyList(@RequestParam(value = "customer_id", required = false) int idParam, Model model, @ModelAttribute Car car, RedirectAttributes redirectAttributes){
		model.addAttribute("customer_id",idParam);
		return "trafficInsuranceForm";
	}
	
	@GetMapping("/carList/{customer_id}")
	public ModelAndView getAllCar(@PathVariable("customer_id") int customer_id) {
		List<Car> cars = carService.getAllCars();
		List<Car> list = new ArrayList<>();
	    for (Car car : cars) {
	        if (car.getCustomer_id() == customer_id && car.getStatus() == 1) {
	        	list.add(car);
	        }
	    }
		return new ModelAndView("carList","car",list);
	}	
	
	@PostMapping("/saveCar") // carEdit.html de kullanılmaktadır
	public String saveCar(@ModelAttribute Car car) {
		int offer = calculate(car);
		car.setOffer(offer);
		carService.save(car);
		return "redirect:/customerList";
	}
	
	@RequestMapping("/editCar/{id}")
	public String editCar(@PathVariable("id") int id, Model model) {
		Car car= carService.getCarId(id);
		model.addAttribute("car", car);
		return "carEdit";
	}
	
	@RequestMapping("/deleteCar/{id}")
	public String deleteCar(@PathVariable("id") int id) {
		Car car = carService.getCarId(id);
		car.setStatus(0);
		carService.save(car);
		//carService.deleteById(id);
		return "redirect:/customerList";
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
