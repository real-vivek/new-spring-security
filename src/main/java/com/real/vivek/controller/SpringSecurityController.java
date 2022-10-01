package com.real.vivek.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringSecurityController {

	@GetMapping("/welcome")
	public String welcome() {
		return "Heyy there! Welcome";
	}
	
	@GetMapping("/contact")
	public String contact() {
		return "Hi from contact page";
	}
	
	@GetMapping("/myCards")
	public String myCards() {
		return "Hi from myCards page";
	}
	
	@GetMapping("/accountInfo")
	public String accountInfo() {
		return "Hi from accountInfo page";
	}
}
