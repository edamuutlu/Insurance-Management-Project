package com.insurance.mgmt.controller;

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
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@Autowired
	CarService carService;

//	@Autowired
//	ProvinceService provinceService;

	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping(path = "/selectType", method = RequestMethod.GET)
	public String selectType(@RequestParam(value = "customerId", required = false) int id, Model model) {
		model.addAttribute("customerId", id);
		return "home";
	}

	@GetMapping("/customerRegisterForm")
	public String registerCustomer(@ModelAttribute Customer customer) {

//	    List<Province> getAllProvinces = provinceRepository.findAll();
//	    
//	    List<District> getDistrictsByProvinceId = districtRepository.findAll();
//
//        model.addAttribute("getAllProvinces", getAllProvinces);
//        model.addAttribute("getDistrictsByProvinceId", getDistrictsByProvinceId);

		return "customerRegisterForm";
	}

	@GetMapping("/customerList")
	public ModelAndView getAllCustomer() {
		List<Customer> list = customerService.getAllCustomer();
		List<Customer> customerList = new ArrayList<>();
		for (Customer customer : list) {
			if (customer.getStatus() == 1) {
				customerList.add(customer);
			}
		}

		return new ModelAndView("customerList", "customer", customerList);
	}

	@PostMapping("/customerRegisterForm")
	public String register(@Valid @ModelAttribute Customer customer, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			log.info(">> Customer : {}", customer.toString());
			return "customerRegisterForm";
		}

		// Aynı TC numaralı ve email adresli kullanıcı kontrolü
		boolean showTcAlert = false;
		boolean showEmailAlert = false;

		List<Customer> list = customerService.getAllCustomer();
		for (Customer c : list) {
			if (c.getStatus() == 1 && c.getTc().equals(customer.getTc())) {
				showTcAlert = true;
			}
			if (c.getStatus() == 1 && c.getEmail().equals(customer.getEmail())) {
				showEmailAlert = true;
			}
		}
		if (showTcAlert == true || showEmailAlert == true) {
			model.addAttribute("showTcAlert", showTcAlert);
			model.addAttribute("showEmailAlert", showEmailAlert);
			return "customerRegisterForm";
		} else {
			model.addAttribute("customers", customerService.getAllCustomer());
			customer.setStatus(1);
			customerService.save(customer);
			return "redirect:customerList";
		}
	}

	@PostMapping("/save")
	public String addCustomer(@ModelAttribute Customer customer, Model model, RedirectAttributes redirectAttributes) {

		// Aynı TC numaralı kullanıcı kontrolü
		boolean showTcAlert = false;
		Customer myCustomer = customerService.getCustomerById(customer.getCustomerId());

		List<Customer> list = customerService.getAllCustomer();
		for (Customer c : list) {
			if (myCustomer.getTc().equals(customer.getTc())) {
				showTcAlert = false;
				break;
			} else if (c.getStatus() == 1 && c.getTc().equals(customer.getTc())) {
				showTcAlert = true;
			}
		}
		if (showTcAlert == true) {
			model.addAttribute("showTcAlert", showTcAlert);
			model.addAttribute("customer", customer);
			return "customerEdit";
		} else {
			customer.setStatus(1);
			customerService.save(customer);
			return "redirect:/customerList";
		}
	}

	@RequestMapping("/editCustomer/{customerId}")
	public String editCustomer(@PathVariable("customerId") int id, Model model) {
		Customer customer = customerService.getCustomerById(id);
		model.addAttribute("customer", customer);
		return "customerEdit";
	}

	@RequestMapping("/deleteCustomer/{customerId}")
	public String deleteCustomer(@PathVariable("customerId") int customerId) {
		List<Car> cars = carService.getAllCars();

		// Müşterinin varsa araçlarının listelenmemesi için
		for (Car car : cars) {
			if (car.getCustomerId() == customerId) {
				car.setResult("Canceled");
				car.setStatus(0);
				carService.save(car);
			} else {
				continue;
			}
		}

		Customer customer = customerService.getCustomerById(customerId);
		customer.setStatus(0);
		customerService.save(customer);
		// customerService.deleteById(customerId); // database den de kalıcı olarak
		// silmek için
		return "redirect:/customerList";
	}

}
