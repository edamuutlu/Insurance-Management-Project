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
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.insurance.mgmt.entity.Home;
import com.insurance.mgmt.entity.Insurance;
import com.insurance.mgmt.repository.IHomeRepository;
import com.insurance.mgmt.repository.IInsuranceRepository;
import com.insurance.mgmt.service.CustomerService;
import com.insurance.mgmt.service.HomeService;
import com.insurance.mgmt.service.InsuranceService;
import com.insurance.mgmt.util.CalculateMethods;

import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	HomeService homeService;
	
	@Autowired
	IHomeRepository homeRepository;
	
	@Autowired
	InsuranceService insuranceService;
	
	@Autowired
	IInsuranceRepository insuranceRepository;
	
	@Autowired
	CustomerService customerService;
	
	private static final Logger log =  LoggerFactory.getLogger(CarController.class);
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	} 
	
	@PostMapping("/homeRegister")
	public String register(@Valid @ModelAttribute Home home, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam(value = "customerId", required = false) int idParam) {		
							
		if(bindingResult.hasErrors()) {
			log.info(">> home : {}",home.toString());
			model.addAttribute("customerId",home.getCustomerId());
			return "homeInsuranceForm";
		}		
		model.addAttribute("homes",homeService.getAllHomes()); 
		
		// Form doldurulurkenki tarih ve sigortanın biteceği tarih hesaplanmaktadır
		Insurance insurance = new Insurance();
		insurance.setCustomerId(idParam);
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		insurance.setStartDate(now.format(formatter));
		LocalDateTime endDate = now.plusDays(insurance.getPeriod());
		insurance.setEndDate(endDate.format(formatter));
        
        CalculateMethods calculateMethods = new CalculateMethods();	// public olan calculate metodunu çağırmak için util'den nesne oluşturulmaktadır
		int offer = calculateMethods.calculateHomeInsurance(home);	
		insurance.setOffer(offer);
		insurance.setStatus(1);	
		
		// Aynı ? kontrolü		
//		List<Insurance> list = insuranceRepository.findByStatus(1);		
//		for (Insurance i : list) {
//			if(  i.getResult().equals("Accepted")) { // KOŞUL EKLENECEK
//				boolean showAlert = true;	
//				model.addAttribute("showAlert",showAlert);				
//				model.addAttribute("customerId",idParam);
//				return "homeInsuranceForm";
//			}			
//		}
		insurance.setInsuranceType("Home");
		insurance.setResult("Canceled");	// Default olarak canceled yazdırılmaktadır
		home.setCustomerId(idParam);
		home.setStatus(1);		
		homeService.save(home);
		insuranceService.save(insurance);
		//redirectAttributes.addFlashAttribute("home", home);				
		//redirectAttributes.addFlashAttribute("insurance",insurance);	
		return "redirect:/customerList";	// homeInsuranceCalculate
				
	}
	
		
	@RequestMapping(path = "/homeInsuranceForm", method = RequestMethod.GET )
	public String getForm(@RequestParam(value = "customerId", required = false) int idParam, Model model, @ModelAttribute Home home){
		model.addAttribute("customerId",idParam);
		return "homeInsuranceForm";
	}
	
	@GetMapping("/homeList/{customerId}")
	public ModelAndView getAllCar(@PathVariable("customerId") int customerId, Model model) {	
		List<Home> homes = homeRepository.findByStatus(1);
		List<Home> list = new ArrayList<>();
			    	    	    
	    for (Home home : homes) {
	        if (home.getCustomerId() == customerId) {
	        	list.add(home);	        	
	        }
	    }
	    
		return new ModelAndView("homeList","home",list);
	}	
	
	@GetMapping("/seeHomeInsuranceDetails/{id}")
	public ModelAndView seeHomeInsuranceDetails(@PathVariable("id") int customerId, Model model) {
		List<Insurance> insurances = insuranceRepository.findByStatus(1);
		List<Insurance> list = new ArrayList<>();
		for(Insurance insurance : insurances) {
			if(insurance.getCustomerId() == customerId) {
				list.add(insurance);
			}
		}		
		return new ModelAndView("seeHomeInsuranceDetails","insurance",list);
	}
	
	@RequestMapping("/deleteHomeInsurance/{id}")
	public String deleteCar(@PathVariable("id") int id, Model model) {
		Insurance insurance = insuranceService.getInsuranceById(id);
		insurance.setResult("Canceled");
		insurance.setStatus(0);
		insuranceService.save(insurance);
		//insuranceService.deleteById(id);	// database den de kalıcı olarak silmek için
		return "redirect:/seeHomeInsuranceDetails/" + insurance.getCustomerId();
	}
		
}
