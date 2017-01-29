package com.example.Controllers;


import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import com.example.Models.Role;
import com.example.Models.User;
import com.example.Models.UserDao;
import com.example.Models.UserServiceImpl;

@Controller
public class BasicController {
/*
 * This controller is used for miscellanious URIs. Personal pages use 'Personal Controller'
 */
	private static final Logger log = LoggerFactory.getLogger(BasicController.class);
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserServiceImpl userService;
	
	@RequestMapping({"/" , "/home"})
    public String index(Model model) {
    	log.info("From the ROOT!");
    	model.addAttribute("name", "Ian");
        return "home";
    }
	
	@RequestMapping("/login")
	public String login(){
		log.info("Hit the login URL");
		return "login";
	}
	
	@RequestMapping("/hello")
	public String hello(){
		log.info("Hit the hello URL");
		return "hello";
	}
	
	@RequestMapping("/getUsers")
	public String getUsers(){
		log.info("Getting all users...");
		userDao.findAll().forEach(u -> log.info(u.toString()));
		log.info("Done getting all users");
		//BCryptPasswordEncoder passEncode = new BCryptPasswordEncoder();
		//log.info("Password Encrypted = " + passEncode.encode("123456"));
		
		return "redirect:/";
	}
	
	@RequestMapping("/saveUser")
	public String createUser(){
		User u = new User();
		u.setUsername("Ian3");
		u.setPassword("12345");
		u.setRoles(new ArrayList<Role>(Arrays.asList(Role.ADMIN, Role.USER)));
		u = userService.saveUser(u);
		
		return "redirect:/";
	}
	
}
