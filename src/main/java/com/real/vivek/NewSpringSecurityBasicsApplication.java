package com.real.vivek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

//This annotation will start spring security in debug mode
@EnableWebSecurity(debug=true)
@SpringBootApplication
public class NewSpringSecurityBasicsApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewSpringSecurityBasicsApplication.class, args);
	}

}
