package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepo;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class Homecontroller {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
    private UserRepo userepo;
	//handler for home page
	@GetMapping("/")
	//@RequestBody
	public String test(Model m) {
		m.addAttribute("title","Home:-Smart contact manager");
		return "home";
	}
	//handler for signup page
	@GetMapping("/signup")
	//@RequestBody
	public String signup(Model m) {
		m.addAttribute("title","Register:-Smart contact manager");
		m.addAttribute("user",new User());
		return "signup";
	}
	
	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("title","Register:-Smart contact manager");
		return "about";
	}
	//handler for do register page
	@PostMapping("/do-register")
	public String registerUser(@Valid @ModelAttribute User user,BindingResult result,@RequestParam(defaultValue="false") boolean agreement,Model m, HttpSession session) {
		try {
			if(!agreement)
			{
				System.out.println("plese check term and conditions");
				throw new Exception("plese check term and conditions");
			}
			if(result.hasErrors())
			{
				System.out.println("Error "+result.toString());
				m.addAttribute("user",user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageurl("disable.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agreement:"+agreement);
			System.out.println("USER:"+user);
			 this.userepo.save(user);
			m.addAttribute("user",new User());
			session.setAttribute("message",new Message("Sucessfully register !!","alert-success"));
			return "signup";
		}catch(Exception e)
		{
			e.printStackTrace();
			m.addAttribute("user",user);
			session.setAttribute("message",new Message("Something went Wrong !!"+e.getMessage(),"alert-danger"));
			return "signup";
		}
		//return "signup";
	}
	//handler for login
	@GetMapping("/signin")
	public String customLogin(Model m)
	{
		m.addAttribute("title","Login page");
		return "login";
	}
}
