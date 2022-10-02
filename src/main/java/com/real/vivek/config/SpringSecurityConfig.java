package com.real.vivek.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
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
	
	//Spring will automatically inject data source which is present in class path in out case it was h2 database
	//When running the app it will run the schema.sql create the tables specified in it and then run data.sql to populate data in those tables
	//We have used custom schema here so we need to tell how to query User by User by UserName and Authorities
	@Bean
	 public JdbcDaoImpl jdbcDaoImpl(DataSource dataSource) {
		JdbcDaoImpl jdbcDaoImpl = new JdbcDaoImpl();
		jdbcDaoImpl.setDataSource(dataSource);
		jdbcDaoImpl.setUsersByUsernameQuery("select uname,pass,enabled from my_users where uname = ?");
		jdbcDaoImpl.setAuthoritiesByUsernameQuery("select uname,authority from my_authorities where uname = ?");
		return jdbcDaoImpl;
	}
	
	//NoOpPasswordEncoder is not recommended for production usage.
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
