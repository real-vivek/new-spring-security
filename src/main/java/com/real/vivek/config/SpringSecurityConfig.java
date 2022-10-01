package com.real.vivek.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {
	
	//Here we create bean of SecurityFilterChain to define custom security config
	//This is done to encourage users to move towards component based security configuration
	//This is the default implementation of Spring security which says:
	//Any request that comes to out application should be authenticated(authorizeRequests().anyRequest().authenticated();)
	//This has to happen for formLogin(request that come through browsers through login form) as well as for httpBasic login(request that have Basic Auth as Authorization in Postman)
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		//Example of denying all any request that comes to out application
		//We get 403 forbidden when we try to hit any endpoint
		http.authorizeRequests().anyRequest().denyAll();
		http.formLogin();
		http.httpBasic();
		return http.build();
	}
}
