package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.smart.entities.User;
import com.smart.repository.UserRepository;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository useRepository;
	
	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","Home - Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title","About - Smart Contact Manager");
		return "about";
	}
	
	
	@RequestMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("title","Register - Smart Contact Manager");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	// this handaler for registring user
	@RequestMapping(value ="/do_register", method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1 ,@RequestParam(value="agreement",defaultValue="false") boolean agreement, Model model,HttpSession session) 
	{ 
		
		try {
			
			if(!agreement)
			{
				System.out.println("You have not agreed the terms and conditions");
				throw new Exception("You have not agreed the terms and conditions");
			}
			
			if(result1.hasErrors()) 
			{
				System.out.println("Error" +result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			
			//password set and encription
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			System.out.println("Agreement" +agreement);
			System.out.println("USER" +user);
			
			User result = this.useRepository.save(user);
			
			model.addAttribute("user",new User());
			//model.addAttribute("user",result);
			
			session.setAttribute("message", new Message("Successfully Registered !!", null, agreement));
			//session.setAttribute("message",new Message("Successfully Registered !!" ,"alert-success", null, null, null, null));
			return "signup";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something Went Wrong !!", null, agreement));
			//session.setAttribute("message",new Message("Something Went Wrong !!" +e.getMessage(),"alert-danger", null, null, e, null));
			return "signup";
		}
		
		
	}
	
	/*
	 * @Autowired private UserRepository userRepository;
	 * 
	 * @GetMapping("/test")
	 * 
	 * @ResponseBody public String test() {
	 * 
	 * 
	 * //com.smart.entities.User user = new com.smart.entities.User(); User user =
	 * new User(); user.setName("pankaj pandit"); user.setEmail("pankaj@gmail.com");
	 * 
	 * Contact contact = new Contact();
	 * 
	 * user.getContacts().add(contact);
	 * 
	 * userRepository.save(user); 
	 * return "Working"; }
	 */

}

