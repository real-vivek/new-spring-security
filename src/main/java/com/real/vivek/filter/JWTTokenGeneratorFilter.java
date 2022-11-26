package com.real.vivek.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.real.vivek.constants.SecurityConstants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

//We want to generate token only once after user is authenticated hence we extend OncePerRequestFilter
//Here in current implementation we are generating new token for each new request
//In the future state we can implement logic to check the expiration time of token and if the token is not expired return the same token
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {
	
	private final Logger LOGGER = LoggerFactory.getLogger(JWTTokenGeneratorFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			LOGGER.info("Generating jwt for user: "+authentication.getName()+" from JWTTokenGeneratorFilter");
			// Here we are retrieve secret key
			SecretKey secret = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes());
			// Here we set issuer(who issuing this JWT), subject
			// We also set user and authorities information using claim method we can also set some other data that we want but we should keep it light weight
			// We also set issuing date and expiration date at last we pass in the secret to generate digital signature
			String jwt = Jwts.builder().setIssuer("rea-vivek").setSubject("JWT Token")
					.claim("username", authentication.getName())
					.claim("authorities", populateAuthirites(authentication.getAuthorities())).setIssuedAt(new Date())
					.setExpiration(new Date(new Date().getTime() + 300000)).signWith(secret).compact();
			LOGGER.info("Setting jwt for user: "+authentication.getName());
			//Here we are setting jwt in header
			response.setHeader(SecurityConstants.JWT_HEADER, jwt);
		}
		filterChain.doFilter(request, response);
	}

	private String populateAuthirites(Collection<? extends GrantedAuthority> authorities) {
		Set<String> authoritiesSet = new HashSet<>();
		for (GrantedAuthority authority : authorities) {
			if (!authoritiesSet.contains(authority.getAuthority())) {
				authoritiesSet.add(authority.getAuthority());
			}
		}
		return String.join(",", authoritiesSet);
	}

}
