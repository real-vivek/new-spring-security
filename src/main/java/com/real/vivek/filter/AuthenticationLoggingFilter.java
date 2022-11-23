package com.real.vivek.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

//This filter will log when BasicAuthentication Filter executes(before or after randomly we cannot predict when spring security will inject it)
public class AuthenticationLoggingFilter implements Filter {

	private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationLoggingFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			LOGGER.info("User " + authentication.getName() + " is being authenticated who has authorities: "
					+ authentication.getAuthorities().toString());
		}
		chain.doFilter(request, response);
	}
	
}
