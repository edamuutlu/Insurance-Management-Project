package com.insurance.mgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.service.CustomerService;

import jakarta.validation.Valid;

@Controller
public class LoginController {
	@Autowired
	CustomerService customerService;
	
	@GetMapping("/")
	public String home() {
		return "index";
	}

	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login(@ModelAttribute Customer customer) {
		return "login";
	}

	@PostMapping("/customerLoggin")
	public String customerLoggin(@Valid @ModelAttribute Customer customer, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {
		List<Customer> customerList = customerService.findByStatus(1);
		
		Customer loggedCustomer = customerService.findByUsername(customer.getUsername());
		System.out.println(loggedCustomer);
		System.out.println(customer);
		
		if(loggedCustomer == null) { 
			model.addAttribute("showAbsentUserAlert", true);
			return "login";
		}else if(loggedCustomer.getStatus()==0) {
			model.addAttribute("showAbsentUserAlert", true);
			return "login";
		}else if(loggedCustomer.getPassword().equals(customer.getPassword())) {
			if(loggedCustomer.getUsername().equals("admin")) {
				model.addAttribute("customer",  customerList);
				return "customerList";
			}
			model.addAttribute("customer",  customer);
			//return "customerInfo";  // kullanıcının sayfasına gitmeli ?
			return "redirect:/customerInfo/" + customer.getUsername();
		}else {
			model.addAttribute("showWrongPasswordAlert", true);
			return "login";
		}	

	}
	
	@GetMapping("/customerInfo/{username}")
	public String customerList(@PathVariable("username") String username, Model model) {
		List<Customer> customer = customerService.findByStatusAndUsername(1, username);
		
		model.addAttribute("customer",  customer);
		return "customerInfo";
	}
}
