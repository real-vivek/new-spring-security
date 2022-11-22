package com.real.vivek.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

//This filter will check if input user name entered consists of the word "test" and will throw 400 if it does
@Component
public class RequestValidationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String header = httpServletRequest.getHeader("Authorization");
		if (header != null) {
			header = header.trim();
			if (StringUtils.startsWithIgnoreCase(header, "Basic")) {
				byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
				byte[] decoded=null;
				try {
					decoded = Base64.getDecoder().decode(base64Token);
					String token = new String(decoded);
					int delim = token.indexOf(":");
					if (delim == -1) {
						throw new BadCredentialsException("Invalid basic authentication token");
					}
					String email = token.substring(0, delim);
					if (email.toLowerCase().contains("test")) {
						httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
				} catch (IllegalArgumentException e) {
					throw new BadCredentialsException("Failed to decode basic authentication token");
				}
			}
		}
		chain.doFilter(httpServletRequest, httpServletResponse);
	}

}
