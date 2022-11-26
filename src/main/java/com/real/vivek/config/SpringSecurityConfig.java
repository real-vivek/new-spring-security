package com.real.vivek.config;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.real.vivek.filter.AuthenticationLoggingFilter;
import com.real.vivek.filter.AuthoritiesLoggingFilter;
import com.real.vivek.filter.JWTTokenGeneratorFilter;
import com.real.vivek.filter.JWTTokenValidatorFilter;
import com.real.vivek.filter.RequestValidationFilter;

@Configuration
public class SpringSecurityConfig {
	
	//Here we create bean of SecurityFilterChain to define custom security config
	//This is done to encourage users to move towards component based security configuration
	//This is the default implementation of Spring security which says:
	//Any request that comes to out application should be authenticated(authorizeRequests().anyRequest().authenticated();)
	//This has to happen for formLogin(request that come through browsers through login form) as well as for httpBasic login(request that have Basic Auth as Authorization in Postman)
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		
		//Here we are telling spring not to generate JESSION ID , we are going to take care of own session/token management. More about spring security session here: https://www.baeldung.com/spring-security-session
		//Even if we use SessionCreationPolicy as NEVER which implies: The framework will never create a session itself, but it will use one if it already exists, we get the JESSION ID when open the URL in browser because any other component of your application (for example, if you would use Spring-Session) is still free to create the session.
		//More info why JSESSION ID is created for browser here: https://stackoverflow.com/questions/52573539/spring-adds-a-jsessionid-despite-stateless-session-management
		//Also we send the token to client in response header so we need to modify some CORS configurations
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()

		//CORS is not security attack but default protection provided by browsers to stop sharing data b/w different origins
		//Here we are configuring CORS globally for entire application
		//We can configure CORS for a particular controller using @CrossOrigin(origins="*")
		.cors().configurationSource(new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration configuration = new CorsConfiguration();
				configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
				configuration.setAllowedMethods(Collections.singletonList("*"));//We are allowing all HTTP Methods
				configuration.setAllowedHeaders(Collections.singletonList("*"));//We are allowing all headers
				configuration.setExposedHeaders(Arrays.asList("Authorization"));//We are allowing to set headers as part of response(only after this browser will accept header from response)
				configuration.setAllowCredentials(true);//We are allowing 
				configuration.setMaxAge(3600L);//We are telling that browser will be allowed to remember this configuration for 1 hr(3600 sec) after this the browser has to do the pre flight check again
				return configuration;
			}
		})
		//CSRF is a security attack where the hacker tries to change user data without consent of user
		//Below is configuration for disabling the CSRF security for some post "/register" endpoint(we don't ahve this endpoint)
		//Whenever we are creating cookie withHttpOnlyFalse, client application will also be able to read the cookie and send it in header or body payload
		//We don't have POST or PUT operation in this application so we will not be able to validate these changes
//		http.csrf().ignoringAntMatchers("/register").csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		//Example of injecting custom filter(RequestValidationFilter) before BasicAuthenticationFilter 
		.and().addFilterBefore(new RequestValidationFilter(), BasicAuthenticationFilter.class)
		//Example of injecting custom filter(AuthoritiesLoggingFilter) after BasicAuthenticationFilter we can validate by logging all the which filters executed
		.addFilterAfter(new AuthoritiesLoggingFilter(), BasicAuthenticationFilter.class)
		//Example of injecting custom filter(AuthoritiesLoggingFilter) at BasicAuthenticationFilter
		.addFilterAt(new AuthenticationLoggingFilter(), BasicAuthenticationFilter.class)
		//Here we are injecting JWTTokenGeneratorFilter in our spring security filter chain just after the user is authenticated(using BasicAuthenticationFilter) so as to generate token and attach it in request header
		.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)		
		//Here we are injecting JWTTokenValidatorFilter in our spring security filter chain just before the user is authenticated(using BasicAuthenticationFilter) so as to if we have any request headers containing token
		//If we have request header containing token we check its validation in this filter
		.addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class);
		http.authorizeRequests().antMatchers("/accountInfo").hasAnyRole("USER","ADMIN");//accountInfo can be accessed by User having role USER or ADMIN
		http.authorizeRequests().antMatchers("/myCards").hasAuthority("READ_WRITE_CARDS");//myCards can be accessed by users having authorities READ_WRITE_CARDS only
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
