package com.smartcontactmanager.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smartcontactmanager.demo.dao.UserReposotery;
import com.smartcontactmanager.demo.entities.User;
import com.smartcontactmanager.demo.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	// spring security

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserReposotery userReposotery;

	@GetMapping("/test")
	@ResponseBody
	public String test() {

		User user = new User();
		user.setName("Mohammed Azhar");
		user.setEmail("mohammedazharmansoori@gmail.com");
		userReposotery.save(user);
		return "working";
	}

	@RequestMapping("/")
	public String home(Model m) {

		m.addAttribute("title", "Home -  smart contact manager");
		return "home";
	}

	@RequestMapping("/about")
	public String About(Model m) {

		m.addAttribute("title", "About - smart contact manager");
		return "about";
	}

	@RequestMapping("/signup")
	public String signup(Model m) {

		m.addAttribute("title", "Register - smart contact manager");
		// sending blank fields
		m.addAttribute("user", new User());
		return "signup";
	}

	// handler for registering user
	/*
	 * @ModelAttribute -- for the items which will match
	 * 
	 * @RequestParam --- which doesnot match
	 * 
	 * @Valid if you are using the server side validater then you have to use this
	 * 
	 * BindingResult
	 */
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session, BindingResult result1) {

		try {

			if (!agreement) {
				System.out.println("you have not agreed the terms and conditions ");
				throw new Exception("you have not agreed the terms and conditions ");
			}

			// validation
			if (result1.hasErrors()) {
				System.out.println("error " + result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}

			// setting the role
			user.setRole("ROLE_USER");
			// enable the
			user.setEnabled(true);
			// setting the image
			user.setImageUrl("default.png");

			// security
			
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			
			
			
		
			
			System.out.println("agreement " + agreement);
			System.out.println("user" + user);

			// saving to db
			User result = this.userReposotery.save(user);

			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("succefully registed  ", "alert-success"));
			return "signup";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("something went wrong " + e.getMessage(), "alert-danger"));
			return "signup";
		}

	}

	// handler for custom login

	@GetMapping("/signin")
	public String customLogin(Model model) {

		model.addAttribute("title", "Login Page");
		return "login";
	}

}
