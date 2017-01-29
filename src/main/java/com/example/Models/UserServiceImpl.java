package com.example.Models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	
	public User saveUser(User u){
		BCryptPasswordEncoder passEncode = new BCryptPasswordEncoder();
		u.setPassword(passEncode.encode(u.getPassword()));
		try{
			u = userDao.save(u);
			log.info("Saving new user: " + u.toString());
		}catch(DataIntegrityViolationException e2){
			log.info("Error saving new user: " + e2);
			u = null;
		}
		return u;
	}
}
