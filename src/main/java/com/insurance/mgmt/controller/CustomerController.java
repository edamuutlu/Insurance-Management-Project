package com.insurance.mgmt.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.service.CustomerService;


@Controller
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
//	@Autowired
//	CarService carService;

	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/form")
	public String addCustomer() {
		return "form";
	}
	
	@GetMapping("/customerRegisterForm")
	public String registerCustomer() {
		return "customerRegisterForm";
	}
	
	
	@GetMapping("/customerList")
	public ModelAndView getAllCustomer() {
		List<Customer> list = customerService.getAllCustomer();
		return new ModelAndView("customerList","customer",list);
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute Customer customer) { 
		System.out.println(customer);
		customerService.save(customer);
		return "redirect:/customerList";
	}
	
	@RequestMapping("/deleteCustomer/{customer_id}")
	public String deleteCustomer(@PathVariable("customer_id") int customer_id) {
		customerService.deleteById(customer_id);
		return "redirect:/customerList";
	}
	
//	@RequestMapping("/trafficInsuranceForm/{id}")
//	public String getMyList(@PathVariable("id") int id) {
//		Car car = carService.getCarById(id);
//		Customer customer = new Customer(car.getId(),car.getCustomer_id());
//		customerService.save(customer);
//		return "redirect:/trafficInsuranceForm";
//	}

}
