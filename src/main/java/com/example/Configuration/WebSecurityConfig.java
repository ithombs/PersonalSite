package com.example.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	//@Autowired
	//DataSource dataSource;
	
	@Autowired
	private UserDetailsImpl userDetails;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
		    	.authorizeRequests()
		        .antMatchers("/", "/home", "/personal", "/personal/*", "/js/**", "/css/**", "/sounds/**","/images/**", "/chess/**").permitAll()
		        .anyRequest().authenticated()
		        .and()
		    .formLogin()
		        .loginPage("/login")
		        .permitAll()
		        .and()
		    .logout()
		        .permitAll();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //auth.inMemoryAuthentication().withUser("test").password("password").roles("USER");
    	auth.userDetailsService(userDetails).passwordEncoder(new BCryptPasswordEncoder());
    }
}
