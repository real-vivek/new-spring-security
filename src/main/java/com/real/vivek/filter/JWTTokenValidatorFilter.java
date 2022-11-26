package com.real.vivek.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.real.vivek.constants.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {
	
	private final Logger LOGGER = LoggerFactory.getLogger(JWTTokenValidatorFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//Here we generally get "Bearer <token-value>" if we hit our app through postman by selecting Authorization type as Bearer Token 
		//For front end applications we have to make custom configurations so that if we send jwt header in response, next time the front end makes request to our app,it send the token in its header as Authorization
		String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
		LOGGER.info("Got Authorization Request header with value: "+jwt+" in JWTTokenValidatorFilter");
		//Here we have additional check to check if token contains the word "Basic" if it contains, then below flow will not be executed
		//This will be the case when the user first tries to login using Basic Authorization as Authorization type
		if (jwt != null && !jwt.contains("Basic")) {
			LOGGER.info("Validating Authorization Request header with value: "+jwt+" in JWTTokenValidatorFilter");
			try {
			//Here we retrieve the secret key
			SecretKey secret = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
			//We create object of claims by passing secret and jwt
			//We only pass substring of jwt because the whole string will contain "Bearer <token-value>" but we want only the <token-value>
			//
			Claims claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(jwt.substring(7)).getBody();
			String username = String.valueOf(claims.get("username"));
			String authorities = String.valueOf(claims.get("authorities"));
			//We create Authentication object ourselves by passing user-name and authorities
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
					AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
			LOGGER.info("Successfully validated Authorization Request header having value: "+jwt+" with username: "+auth.getName());
			//Spring security assume that authentication is successful after this and hence we need to keep authentication object in SecurityContextHolder
			SecurityContextHolder.getContext().setAuthentication(auth);
			}
			//When JWT libraries are trying to pass jwt based on secret key that we have passed they internally calculate hash value is matching or not  if not then they throw different exceptions
			//To handle these exceptions like ExpiredJwtException, MalformedJwtException, SignatureException we have written this catch block where we throw bad credentials exception
			catch (Exception e) {
				LOGGER.error("Error when validatin Authorization Request header with value: "+jwt+" in JWTTokenValidatorFilter: "+e.getStackTrace());
				throw new BadCredentialsException("Invalid Token received");
			}
		}
		filterChain.doFilter(request, response);
	}
}
