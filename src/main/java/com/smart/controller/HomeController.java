package com.smart.controller;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserRepository userRepo;
	@GetMapping("/")
//	@ResponseBody
	public String home(Model m) {
		m.addAttribute("title", "Home-Smart Contact Manager");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("title", "About-Smart Contact Manager");
		return "about";
	}
	@GetMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("user", new User());
		m.addAttribute("title", "Register-Smart Contact Manager");
		return "signup";
	}
	
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
			Model m, HttpSession session) {
		
		try {
			if(!agreement) {
				throw new Exception("You must agree terms and conditions");
				
			}
			if(result.hasErrors()) {
				m.addAttribute("user", user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			System.out.print(agreement);
			System.out.print(user);
			User result1 = userRepo.save(user);
			m.addAttribute("user", new User());
			session.setAttribute("message", new Message("SuccessFully Registered", "alert-success"));
			
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went Wrong -> "+ e.getMessage(), "alert-danger"));
			return "signup";
		}
		
		
	}
	@GetMapping("/signin")
	public String customLogin(Model m) {
		m.addAttribute("title", "Login Page");
		return "login";
	}
}
