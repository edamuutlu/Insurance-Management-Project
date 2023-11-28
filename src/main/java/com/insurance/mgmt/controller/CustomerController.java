package com.insurance.mgmt.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.insurance.mgmt.entity.Car;
import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.entity.Health;
import com.insurance.mgmt.entity.Home;
import com.insurance.mgmt.entity.Insurance;
import com.insurance.mgmt.entity.Kdv;
import com.insurance.mgmt.entity.address.District;
import com.insurance.mgmt.entity.address.Province;
import com.insurance.mgmt.service.CarService;
import com.insurance.mgmt.service.CustomerService;
import com.insurance.mgmt.service.HealthService;
import com.insurance.mgmt.service.HomeService;
import com.insurance.mgmt.service.InsuranceService;
import com.insurance.mgmt.service.KdvService;
import com.insurance.mgmt.service.address.DistrictService;
import com.insurance.mgmt.service.address.ProvinceService;

import jakarta.validation.Valid;

@Controller
public class CustomerController {

	@Autowired
	CustomerService customerService; 

	@Autowired
	CarService carService;
	
	@Autowired
	HomeService homeService;
	
	@Autowired
	HealthService healthService;
	
	@Autowired
	InsuranceService insuranceService;

	@Autowired
	ProvinceService provinceService;
	
	@Autowired
	DistrictService districtService;
	
	@Autowired
	KdvService kdvService;
			

	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/")
	public String home() {
		return "index";
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
	
	@GetMapping("/customerRegister")
	public String customerRegister(@ModelAttribute Customer customer, Model model) {
		
		List<Province> getAllProvinces = provinceService.listAll();
		List<District> getAllDistricts = districtService.listAll();

        model.addAttribute("getAllProvinces", getAllProvinces);
        model.addAttribute("getAllDistricts", getAllDistricts);

		return "customerRegister";
	}

	@GetMapping("/customerRegisterForm")
	public String customerRegisterForm(@ModelAttribute Customer customer, Model model) {
		
		List<Province> getAllProvinces = provinceService.listAll();
		List<District> getAllDistricts = districtService.listAll();

        model.addAttribute("getAllProvinces", getAllProvinces);
        model.addAttribute("getAllDistricts", getAllDistricts);

		return "customerRegisterForm";
	}

	@GetMapping("/customerList")
	public String customerList(Model model) {
		List<Customer> customerList = customerService.findByStatus(1);
		
		Kdv carKdv = kdvService.getProductTypeById(1);
		Kdv homeKdv = kdvService.getProductTypeById(2);
		Kdv healthKdv = kdvService.getProductTypeById(3);
		model.addAttribute("carKdv", carKdv);
		model.addAttribute("homeKdv", homeKdv);
		model.addAttribute("healthKdv", healthKdv);
		
		model.addAttribute("customer",  customerList);
		
		return "customerList";
	}
	
	@PostMapping("/saveKdv")
	public String saveKdv(@RequestParam int kdvRate, @RequestParam int productType) {		
		Kdv kdv = kdvService.getProductTypeById(productType);
		kdv.setKdvRate(kdvRate);
		kdvService.save(kdv);
		return "redirect:/customerList";				
	}

	@PostMapping("/customerRegister")
	public String customerRegister(@Valid @ModelAttribute Customer customer,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		List<Province> getAllProvinces = provinceService.listAll();
		List<District> getAllDistricts = districtService.listAll();
		
//		if (bindingResult.hasErrors()) {
//			log.info(">> Customer : {}", customer.toString());
//			
//	        model.addAttribute("getAllProvinces", getAllProvinces);
//	        model.addAttribute("getAllDistricts", getAllDistricts);
//			
//			return "customerRegister";
//		}
//
//		// Aynı TC numaralı ve email adresli kullanıcı kontrolü
//		List<Customer> sameTcList = customerService.findByStatusAndTc(1, customer.getTc());
//		List<Customer> sameEmailList = customerService.findByStatusAndEmail(1, customer.getEmail());
//
//		boolean showTcAlert = !sameTcList.isEmpty();
//		boolean showEmailAlert = !sameEmailList.isEmpty();
//
//		if (showTcAlert || showEmailAlert) {
//		    model.addAttribute("showTcAlert", showTcAlert);
//		    model.addAttribute("showEmailAlert", showEmailAlert);
//		    model.addAttribute("getAllProvinces", getAllProvinces);
//		    model.addAttribute("getAllDistricts", getAllDistricts);
//		    return "customerRegister";
//		} else {
//		    customer.setStatus(1);
//		    customerService.save(customer);
//		    return "redirect:customerList";
//		}
		customer.setStatus(1);
	    customerService.save(customer);
		 return "redirect:customerList";
	}

	@PostMapping("/save")
	public String saveCustomer(@ModelAttribute Customer customer, Model model, RedirectAttributes redirectAttributes) {

		// Aynı TC numaralı kullanıcı kontrolü
		Customer myCustomer = customerService.getCustomerById(customer.getCustomerId());

		List<Customer> list = customerService.findByStatus(1);
		for (Customer c : list) {
			if (myCustomer.getTc().equals(customer.getTc())) {
				customer.setStatus(1);
				customerService.save(customer);
				model.addAttribute("showTcAlert", false);
				return "redirect:/customerList";
			} else if (c.getTc().equals(customer.getTc())) {
				model.addAttribute("showTcAlert", true);
				model.addAttribute("customer", customer);
				return "customerEdit";
			}
		}
		customer.setStatus(1);
		customerService.save(customer);
		return "redirect:/customerList";
	}

	@RequestMapping("/editCustomer/{customerId}")
	public String editCustomer(@PathVariable("customerId") int id, Model model) {
		List<Province> getAllProvinces = provinceService.listAll();
		List<District> getAllDistricts = districtService.listAll();

        model.addAttribute("getAllProvinces", getAllProvinces);
        model.addAttribute("getAllDistricts", getAllDistricts);
		
		Customer customer = customerService.getCustomerById(id);
		model.addAttribute("customer", customer);
		return "customerEdit";
	}

	@RequestMapping("/deleteCustomer/{customerId}")
	public String deleteCustomer(@PathVariable("customerId") int customerId) {
		
		// Müşterinin varsa araçlarının listelenmemesi için
		List<Car> cars = carService.findByStatusAndCustomerId(1, customerId); 
		List<Home> homes = homeService.findByStatusAndCustomerId(1, customerId);
		List<Health> healthInfos = healthService.findByStatusAndCustomerId(1, customerId);
		List<Insurance> insurances = insuranceService.findByStatusAndCustomerId(1, customerId);
		for (Car car : cars) {
			car.setStatus(0);
			carService.save(car);
		}
		for (Home home : homes) {
			home.setStatus(0);
			homeService.save(home);
		}
		for (Health health : healthInfos) {
			health.setStatus(0);
			healthService.save(health);
		}
		for (Insurance insurance : insurances) {
			insurance.setResult("Canceled");
			insurance.setStatus(0);
			insuranceService.save(insurance);
		}

		Customer customer = customerService.getCustomerById(customerId);
		customer.setStatus(0);
		customerService.save(customer);
		return "redirect:/customerList";
	}

}
