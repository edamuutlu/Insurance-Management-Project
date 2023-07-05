package com.insurance.mgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.insurance.mgmt.entity.User;
import com.insurance.mgmt.repository.IUserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
	@Autowired 
	private IUserRepository userRepository;

	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/form")
	public String bookRegister() {
		return "form";
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute User user, HttpSession session) { 
		System.out.println(user);
		userRepository.save(user);
		session.setAttribute("message", "User Register Successfully...");
		return "redirect:/form";
	}

}
