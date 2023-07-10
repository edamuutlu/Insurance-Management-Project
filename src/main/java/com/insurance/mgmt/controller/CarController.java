package com.insurance.mgmt.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

@Controller
public class CarController {
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	CarService carService;
	
	@PostMapping("/carRegister")
	public String register(@ModelAttribute Car car, Model model, RedirectAttributes redirectAttributes) { 
			
		
		// Sigorta teklifi için hesaplamalar
		int offer=0;
		
		switch (car.getType()) {
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
	
		switch (car.getPurpose()) {
		    case "Private":
		        offer += 4000;
		        break;
		    case "Commercial":
		        offer += 5000;
		        break;
		    default:
				break;
		}
		
		switch (car.getBrand()) {
			case "Audi":
			case "BMW":
			case "Lamborghini":
			case "Mercedes":
			case "Volvo":
				offer+=1000;
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
				offer+=3000;
				break;
			default:
				break;
		}
		
		switch (car.getFuel_type()) {
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
		
		if(car.getEngine_size()<2) {
			offer+=3500;
		}else if(car.getEngine_size()>2) {
			offer+=4000;
		}
		
		switch (car.getLicense_plate1()) {
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
		
		if(car.getSeat_capacity()<=2) {
			offer+=3000;
		}else if(car.getSeat_capacity()>2 && car.getSeat_capacity()<=5) {
			offer+=3500;
		}else if(car.getSeat_capacity()>5) {
			offer+=4000;
		}
		
		car.setOffer(offer);
		carService.save(car);
		
		redirectAttributes.addFlashAttribute("car", car);
		
		Customer customer = customerService.getCustomerById(car.getCustomer_id());
		
		redirectAttributes.addFlashAttribute("customer",customer);		
		
		return "redirect:/trafficInsuranceCalculate";
	}
	
	@GetMapping("/trafficInsuranceCalculate")
	public String trafficInsurance(RedirectAttributes redirectAttributes, Model model) {
		
		return "trafficInsuranceCalculate";
	}
	
//	@RequestMapping("/editCar/{id}")
//	public String editBook(@PathVariable("id") int id, Model model) {
//		Car car= carService.getCarId(id);
//		model.addAttribute("car", car);
//		return "editCar";
//	}
	
	@RequestMapping(path = "/trafficInsuranceForm", method = RequestMethod.GET )
	public String getMyList(@RequestParam(value = "customer_id", required = false) int idParam, Model model){
		model.addAttribute("customer_id",idParam);
		return "trafficInsuranceForm";
	}
	
	@GetMapping("/carList/{customer_id}")
	public ModelAndView getAllCar(@PathVariable("customer_id") int customer_id) {
		List<Car> cars = carService.getAllCars();
		List<Car> list = new ArrayList<>();
	    for (Car car : cars) {
	        if (car.getCustomer_id() == customer_id) {
	        	list.add(car);
	        }
	    }
		return new ModelAndView("carList","car",list);
	}	

}
