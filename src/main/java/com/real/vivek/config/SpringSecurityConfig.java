package com.real.vivek.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
		// Remember to go from most restrictive to least restrictive role
		http.authorizeRequests().antMatchers("/accountInfo").hasAnyRole("USER","ADMIN");//accountInfo can be accessed by User having role USER or ADMIN
		http.authorizeRequests().antMatchers("/myCards").hasRole("USER");//myCards can be accessed by User having role USER
		http.authorizeRequests().antMatchers("/contact","/welcome").permitAll();
		http.formLogin();
		http.httpBasic();
		return http.build();
	}
	
	//We have implemented our own custom user details service so no need to create bean of UserDetailsManager interface
	//Since H2 has it's own authentication provider, you can skip the Spring Security for the path of h2 console entirely in the same way that you do for your static content
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2-console/**");
    }
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
