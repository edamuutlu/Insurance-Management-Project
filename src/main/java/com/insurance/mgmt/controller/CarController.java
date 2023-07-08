package com.insurance.mgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.insurance.mgmt.entity.Car;
import com.insurance.mgmt.service.CarService;
import com.insurance.mgmt.service.CustomerService;

@Controller
public class CarController {
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	CarService carService;
	
//	@GetMapping("/trafficInsuranceForm")
//	public String trafficInsurance() {
//		return "trafficInsuranceForm"; // model ekle
//	}
			
	@PostMapping("/carRegister")
	public String register(@ModelAttribute Car car) { 
		carService.save(car); 	
		return "redirect:/customerList";
	}
	
//	@RequestMapping("/editCar/{id}")
//	public String editBook(@PathVariable("id") int id, Model model) {
//		Car car= carService.getCarId(id);
//		model.addAttribute("car", car);
//		return "editCar";
//	}
	
//	@RequestMapping("/trafficInsuranceForm/{customer_id}")
//	public String getMyList(@PathVariable("customer_id") int id, Model model) {
//		//Customer customer = customerService.getCustomerById(id);
//		//model.addAttribute("customer", customer);
//		return "trafficInsuranceForm";
//	}
	
	@RequestMapping(path = "/trafficInsuranceForm", method = { RequestMethod.GET, RequestMethod.POST } )
	public String getMyList(@RequestParam(value = "customer_id", required = false) int idParam, Model model){
		//Customer customer = customerService.getCustomerById(id);
		//model.addAttribute("customer", customer);
		model.addAttribute("customer_id",idParam);
		return "trafficInsuranceForm";
	}

	
//	@GetMapping("/trafficInsuranceForm")
//	public String getCars(Model model) {
//		List<Car> list = carService.getAllCars();
//		model.addAttribute("car",list);
//		return "trafficInsuranceForm";
//	}	

}
