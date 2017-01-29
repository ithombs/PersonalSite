package com.example.Configuration;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Models.User;
import com.example.Models.UserDao;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
@Transactional
public class UserDetailsImpl implements UserDetailsService{

	@Autowired
	private UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		org.springframework.security.core.userdetails.User authedUser;
		User user = userDao.findByUsername(username);
		
		if(user == null){
			throw new UsernameNotFoundException("***Username does not exist***");
		}
		
		authedUser = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getRoles(user));
		
		
		return authedUser;
	}
	
	private List<SimpleGrantedAuthority> getRoles(User u){
		List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
		u.getRoles().forEach(role -> roles.add(new SimpleGrantedAuthority(role.toString())));
		
		return roles;
	}

}
