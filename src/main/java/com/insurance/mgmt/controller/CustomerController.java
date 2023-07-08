package com.insurance.mgmt.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.service.CarService;
import com.insurance.mgmt.service.CustomerService;


@Controller
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	CarService carService;
	
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/form")
	public String customerForm() {
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
	
	@PostMapping("/save")
	public String addCustomer(@ModelAttribute Customer customer) {
		customerService.save(customer);
		return "redirect:/customerList";
	}
	
	@RequestMapping("/editCustomer/{customer_id}")
	public String editCustomer(@PathVariable("customer_id") int id, Model model) {
		Customer customer= customerService.getCustomerById(id);
		model.addAttribute("customer", customer);
		return "customerEdit";
	}
	
	@RequestMapping("/deleteCustomer/{customer_id}")
	public String deleteCustomer(@PathVariable("customer_id") int customer_id) {
		customerService.deleteById(customer_id);
		return "redirect:/customerList";
	}
	
//	@RequestMapping("/trafficInsuranceForm/{customer_id}")
//	public String getMyList(@PathVariable("customer_id") int customer_id, ModelAttribute model) {
//		Customer customer = customerService.getCustomerById(customer_id);
//		Car car = new Car(customer.getCustomer_id());         
//		carService.save(car);
//		return "redirect:/trafficInsuranceForm";
//	}
		

}
