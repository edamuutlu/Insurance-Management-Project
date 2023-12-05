package com.insurance.mgmt.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import com.insurance.mgmt.entity.Car;
import com.insurance.mgmt.entity.CarInsurance;
import com.insurance.mgmt.entity.HealthInsurance;
import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.entity.Health;
import com.insurance.mgmt.entity.Home;
import com.insurance.mgmt.service.CarInsuranceService;
import com.insurance.mgmt.service.CarService;
import com.insurance.mgmt.service.CustomerService;
import com.insurance.mgmt.service.HealthInsuranceService;
import com.insurance.mgmt.service.HealthService;
import com.insurance.mgmt.service.HomeInsuranceService;
import com.insurance.mgmt.service.HomeService;

import jakarta.validation.Valid;

@Controller
public class LoginController {
	@Autowired
	CustomerService customerService;

	@Autowired
	HealthService healthService;

	@Autowired
	HomeService homeService;

	@Autowired
	CarService carService;

	@Autowired
	CarInsuranceService carInsuranceService;

	@Autowired
	HomeInsuranceService homeInsuranceService;

	@Autowired
	HealthInsuranceService healthInsuranceService;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

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

		if (loggedCustomer == null) {
			model.addAttribute("showAbsentUserAlert", true);
			return "login";
		} else if (loggedCustomer.getStatus() == 0) {
			model.addAttribute("showAbsentUserAlert", true);
			return "login";
		} else if (loggedCustomer.getPassword().equals(customer.getPassword())) {
			if (loggedCustomer.getUsername().equals("admin")) {
				model.addAttribute("customer", customerList);
			    return "redirect:/customerList";
			} else {
				model.addAttribute("customer", customer);
				// return "redirect:/customerInfo/" + customer.getUsername();
				return "redirect:/userPage/" + customer.getUsername();
			}
		} else {
			model.addAttribute("showWrongPasswordAlert", true);
			return "login";
		}

	}

//	@GetMapping("/customerInfo/{username}")
//	public String customerInfo(@PathVariable("username") String username, Model model) {
//		List<Customer> customer = customerService.findByStatusAndUsername(1, username);
//		
//		model.addAttribute("customer",  customer);
//		return "customerInfo";
//	}

	@GetMapping("/userPage/{username}")
	public String userPage(@PathVariable("username") String username, Model model) {
		Customer customer = customerService.findByUsername(username);
		List<Health> health = healthService.findByStatusAndCustomerId(1, customer.getCustomerId());
		List<Home> home = homeService.findByStatusAndCustomerId(1, customer.getCustomerId());
		List<Car> car = carService.findByStatusAndCustomerId(1, customer.getCustomerId());

		// CAR
		List<Car> cars = carService.findByStatusAndCustomerId(1, customer.getCustomerId()); //?
		List<CarInsurance> insurances = carInsuranceService.findByStatusAndCustomerIdAndResult(1,
				customer.getCustomerId(), "Accepted");
		ArrayList<Car> expiredCars = new ArrayList<>();

		// Poliçenin süresinin bitip bitmediğini kontrol etme
		LocalDateTime now = LocalDateTime.now();
		for (CarInsurance insurance : insurances) {
			LocalDateTime endDateTime = LocalDateTime.parse(insurance.getEndDate(), formatter);
			if (now.isAfter(endDateTime)) {
				Car expiredCar = carService.getCarId(insurance.getCarId());
				expiredCars.add(expiredCar);
				model.addAttribute("showText", true);
				insurance.setResult("Expired");
				carInsuranceService.save(insurance);
			}
		}
		model.addAttribute("expiredCars", expiredCars);
		model.addAttribute("carList", cars);

		// HEALTH
		List<Health> healthInfos = healthService.findByStatusAndCustomerId(1, customer.getCustomerId());
		List<HealthInsurance> healthInsurances = healthInsuranceService.findByStatusAndCustomerIdAndResult(1,
				customer.getCustomerId(), "Accepted");
		ArrayList<Health> expiredHealthInsurance = new ArrayList<>();

		// Poliçenin süresinin bitip bitmediğini kontrol etme
		for (HealthInsurance insurance : healthInsurances) {
			LocalDateTime endDateTime = LocalDateTime.parse(insurance.getEndDate(), formatter);

			if (now.isAfter(endDateTime)) {
				Health expiredHealth = healthService.getHealthById(insurance.getHealthId());
				expiredHealthInsurance.add(expiredHealth);
				model.addAttribute("showExpiredAlert", true);
				insurance.setResult("Expired");
				healthInsuranceService.save(insurance);
			}
		}

		// Sağlık bilgilerini güncelleme süresinin bitip bitmediğini kontrol etme
		for (Health h : healthInfos) {
			LocalDateTime deadlineDate = LocalDateTime.parse(h.getDeadline(), formatter);

			if (now.isAfter(deadlineDate)) {
				model.addAttribute("showDeadlineAlert", true);
				model.addAttribute("healthId", h.getHealthId());
			}
		}

		model.addAttribute("expiredHealthInsurance", expiredHealthInsurance);
		model.addAttribute("healthInfoList", healthInfos);

		// HOME

		model.addAttribute("customer", customer);
		model.addAttribute("health", health);
		model.addAttribute("home", home);
		model.addAttribute("car", car);
		return "userPage";
	}
}
