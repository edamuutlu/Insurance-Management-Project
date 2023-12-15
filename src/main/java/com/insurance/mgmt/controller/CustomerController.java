package com.insurance.mgmt.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.insurance.mgmt.entity.Company;
import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.entity.Kdv;
import com.insurance.mgmt.entity.address.District;
import com.insurance.mgmt.entity.address.Province;
import com.insurance.mgmt.repository.IInsuranceRepository;
import com.insurance.mgmt.service.CarInsuranceService;
import com.insurance.mgmt.service.CarService;
import com.insurance.mgmt.service.CompanyService;
import com.insurance.mgmt.service.CustomerService;
import com.insurance.mgmt.service.HealthInsuranceService;
import com.insurance.mgmt.service.HealthService;
import com.insurance.mgmt.service.HomeInsuranceService;
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
	CarInsuranceService carInsuranceService;

	@Autowired
	HomeInsuranceService homeInsuranceService;

	@Autowired
	HealthInsuranceService healthInsuranceService;

	@Autowired
	ProvinceService provinceService;

	@Autowired
	DistrictService districtService;

	@Autowired
	KdvService kdvService;

	@Autowired
	CompanyService companyService;

	@Autowired
	IInsuranceRepository insuranceRepo;
	
	@Autowired
	InsuranceService insuranceService;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@RequestMapping(path = "/selectType", method = RequestMethod.GET)
	public String selectType(@RequestParam(value = "customerId", required = false) int customerId, Model model) {
		model.addAttribute("customerId", customerId);
		Customer customer = customerService.getCustomerById(customerId);
		model.addAttribute("customer", customer);
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

	@GetMapping("/customerList")
	public String customerList(Model model) {
		//List<Customer> customerList = customerService.findByStatus(1);
		List<Customer> customerList = customerService.getCustomersWithIdGreaterThanOne(1);

		Kdv carKdv = kdvService.getProductTypeById(1);
		Kdv homeKdv = kdvService.getProductTypeById(2);
		Kdv healthKdv = kdvService.getProductTypeById(3);
		model.addAttribute("carKdv", carKdv);
		model.addAttribute("homeKdv", homeKdv);
		model.addAttribute("healthKdv", healthKdv);

		List<Company> companyList = companyService.getAllCompany();
		model.addAttribute("company", companyList);

		// Sigorta Detayların Hepsini Görüntülemek için
		List<Object[]> resultList = insuranceRepo.getUnionAllResult();
		List<List<Object>> allInsurances = new ArrayList<>();

		for (Object[] row : resultList) {
		    List<Object> rowData = new ArrayList<>();
		    for (Object column : row) {
		        rowData.add(column);
		    }
		    allInsurances.add(rowData);
		}

		model.addAttribute("allInsurances", allInsurances);
		
		/*DISTINCT ile sigorta tipine göre listelenmek istenirse
		List<String> productTypes = insuranceRepo.getAllProductType();
		System.out.println(productTypes);
		model.addAttribute("productTypes", productTypes);*/

		// Sigorta tipine göre şirket bilançoları
		// Car Balances
		List<Object[]> getAllCarInsuranceBalance = insuranceRepo.getCarInsuranceBalance();
		List<List<Object>> carInsuranceBalances = new ArrayList<>();

		for (Object[] row : getAllCarInsuranceBalance) {
		    List<Object> rowData = new ArrayList<>();
		    for (Object column : row) {
		        rowData.add(column);
		    }
		    carInsuranceBalances.add(rowData);
		}
		model.addAttribute("carBalances", carInsuranceBalances);
		
		//Home Balances
		List<Object[]> getHomeInsuranceBalance = insuranceRepo.getHomeInsuranceBalance();
		List<List<Object>> homeInsuranceBalances = new ArrayList<>();

		for (Object[] row : getHomeInsuranceBalance) {
		    List<Object> rowData = new ArrayList<>();
		    for (Object column : row) {
		        rowData.add(column);
		    }
		    homeInsuranceBalances.add(rowData);
		}
		model.addAttribute("homeBalances", homeInsuranceBalances);
		
		// Health Balances
		List<Object[]> getHealthInsuranceBalance = insuranceRepo.getHealthInsuranceBalance();
		List<List<Object>> healthInsuranceBalances = new ArrayList<>();

		for (Object[] row : getHealthInsuranceBalance) {
		    List<Object> rowData = new ArrayList<>();
		    for (Object column : row) {
		        rowData.add(column);
		    }
		    healthInsuranceBalances.add(rowData);
		}
		model.addAttribute("healthBalances", healthInsuranceBalances);
		System.out.println(healthInsuranceBalances);

		LocalDateTime now = LocalDateTime.now();
		model.addAttribute("now", now.format(formatter));
		model.addAttribute("customer", customerList);
		return "customerList";
	}

	@PostMapping("/saveKdv")
	public String saveKdv(@RequestParam int kdvRate, @RequestParam int productType) {
		//Kdv kdv = kdvService.getProductTypeById(productType);
		//kdv.setKdvRate(kdvRate);
		//kdvService.save(kdv);
		
		insuranceService.updateData(kdvRate, productType);
		return "redirect:/customerList";
	}

	@PostMapping("/customerRegister")
	public String customerRegister(@Valid @ModelAttribute Customer customer, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {
		List<Province> getAllProvinces = provinceService.listAll();
		List<District> getAllDistricts = districtService.listAll();

		if (bindingResult.hasErrors()) {
			log.info(">> Customer : {}", customer.toString());

			model.addAttribute("getAllProvinces", getAllProvinces);
			model.addAttribute("getAllDistricts", getAllDistricts);

			return "customerRegister";
		}

		// Aynı TC numaralı, email adresli ve username adlı kullanıcı kontrolü
		List<Customer> sameTcList = customerService.findByStatusAndTc(1, customer.getTc());
		List<Customer> sameEmailList = customerService.findByStatusAndEmail(1, customer.getEmail());
		List<Customer> sameUsernameList = customerService.findByStatusAndUsername(1, customer.getUsername());

		boolean showTcAlert = !sameTcList.isEmpty();
		boolean showEmailAlert = !sameEmailList.isEmpty();
		boolean showUsernameAlert = !sameUsernameList.isEmpty();

		if (showTcAlert || showEmailAlert || showUsernameAlert) {
			model.addAttribute("showTcAlert", showTcAlert);
			model.addAttribute("showEmailAlert", showEmailAlert);
			model.addAttribute("showUsernameAlert", showUsernameAlert);
			model.addAttribute("getAllProvinces", getAllProvinces);
			model.addAttribute("getAllDistricts", getAllDistricts);
			return "customerRegister";
		} else {
			customer.setStatus(1);
			customerService.save(customer);
			return "redirect:/userPage/" + customer.getUsername();
		}

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
				return "redirect:/userPage/" + myCustomer.getUsername();
			} else if (c.getTc().equals(customer.getTc())) {
				model.addAttribute("showTcAlert", true);
				model.addAttribute("customer", customer);
				return "customerEdit";
			}
		}
		customer.setStatus(1);
		customerService.save(customer);
		return "redirect:/userPage/" + myCustomer.getUsername();
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

	@RequestMapping(path = { "/deleteCustomer/{customerId}",
			"/deleteCustomer/{customerId}/{admin}" }, method = RequestMethod.GET)
	public String deleteCustomer(@PathVariable("customerId") int customerId,
			@PathVariable(required = false) String admin, Model model) {
		/*
		// Müşterinin varsa araçlarının listelenmemesi için
		List<Car> cars = carService.findByStatusAndCustomerId(1, customerId);
		List<Home> homes = homeService.findByStatusAndCustomerId(1, customerId);
		List<Health> healthInfos = healthService.findByStatusAndCustomerId(1, customerId);
		List<CarInsurance> carInsurances = carInsuranceService.findByStatusAndCustomerId(1, customerId);
		List<HomeInsurance> homeInsurances = homeInsuranceService.findByStatusAndCustomerId(1, customerId);
		List<HealthInsurance> healthInsurances = healthInsuranceService.findByStatusAndCustomerId(1, customerId);
		
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
		for (CarInsurance insurance : carInsurances) {
			insurance.setResult("Canceled");
			insurance.setStatus(0);
			carInsuranceService.save(insurance);
		}
		for (HomeInsurance insurance : homeInsurances) {
			insurance.setResult("Canceled");
			insurance.setStatus(0);
			homeInsuranceService.save(insurance);
		}
		for (HealthInsurance insurance : healthInsurances) {
			insurance.setResult("Canceled");
			insurance.setStatus(0);
			healthInsuranceService.save(insurance);
		}
		*/
		
		insuranceService.deleteCarInsuranceData(customerId);
		insuranceService.deleteHomeInsuranceData(customerId);
		insuranceService.deleteHealthInsuranceData(customerId);
		insuranceService.deleteCarData(customerId);
		insuranceService.deleteHomeData(customerId);
		insuranceService.deleteHealthData(customerId);
		insuranceService.deleteCustomerData(customerId);

		/*Customer customer = customerService.getCustomerById(customerId);
		customer.setStatus(0);
		customerService.save(customer); */
		
		List<Customer> customerList = customerService.findByStatus(1);
		model.addAttribute("customer", customerList);
		return "redirect:/customerList";

	}

}
