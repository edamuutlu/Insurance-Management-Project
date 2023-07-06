package com.insurance.mgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.insurance.mgmt.entity.Car;
import com.insurance.mgmt.service.CarService;

@Controller
public class CarController {
	
	@Autowired
	CarService carService;
	
	@GetMapping("/trafficInsuranceForm")
	public String trafficInsurance() {
		return "trafficInsuranceForm";
	}
	
	@PostMapping("/carRegister")
	public String register(@ModelAttribute Car car) { 
		carService.save(car);
		return "redirect:/trafficInsuranceForm";
	}
	
//	@RequestMapping("/trafficInsuranceForm/{customer_id}")
//	public String getMyList(@PathVariable("customer_id") int customer_id, ModelAttribute model) {
//		Customer customer = customerService.getCarId(customer_id);	
//		model.addAttribute("customer",customer);
//		car.setId(customer_id);
//		carService.save(car);
//		return "redirect:/trafficInsuranceForm";
//	}

}
