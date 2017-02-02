package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example", "com.example.Models"})
public class SpringBootTest2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTest2Application.class, args);
	}
}
