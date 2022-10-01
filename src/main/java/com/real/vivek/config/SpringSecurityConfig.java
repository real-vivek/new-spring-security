package com.real.vivek.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
	
	@Bean
    public InMemoryUserDetailsManager userDetailsService() {
		//If we use below config we will not have to provide bean of type PasswordEncoder
		UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("12345")
                .authorities("admin")
                .roles("ADMIN")//Here we add ADMIN role to this user
                .build();
		//However for below config we have to provide to create bean of type PasswordEncoder and register it with spring
        UserDetails user = User.builder()
                .username("user")
                .password("12345")
                .authorities("read")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin, user);
	}
	
	//NoOpPasswordEncoder is not recommended for production usage.
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
