package com.real.vivek.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.real.vivek.beans.Authority;
import com.real.vivek.beans.Customer;
import com.real.vivek.repo.CustomerRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private MultiTableDetailsService multiTableDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String pwd = authentication.getCredentials().toString();

		// Logic to load users from customerRepository and getAuthorities
		if (username.contains("@")) {
			List<Customer> customers = customerRepository.findByEmail(username);
			if (customers.size() > 0) {
				if (passwordEncoder.matches(pwd, customers.get(0).getPwd())) {
					return new UsernamePasswordAuthenticationToken(username, pwd,
							getGrantedAuthorities(customers.get(0).getAuthorities()));
				} else {
					throw new BadCredentialsException("Invalid password");
				}
			} else {
				throw new BadCredentialsException("No user registered with this details!");
			}
		}
		// Logic to load in-memory users and getAuthorities
		else {
			UserDetailsService defaultInMemoryUserDetailsService = multiTableDetailsService.jdbcDaoImpl(dataSource);
			UserDetails inMemoryUser = defaultInMemoryUserDetailsService.loadUserByUsername(username);
			if (inMemoryUser != null) {
				if (passwordEncoder.matches(pwd, inMemoryUser.getPassword())) {
					return new UsernamePasswordAuthenticationToken(username, pwd, inMemoryUser.getAuthorities());
				} else {
					throw new BadCredentialsException("Invalid password");
				}
			} else {
				throw new BadCredentialsException("No user registered with this details!");
			}

		}

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	// If we pass set of authorities from backend db, this helper method will read
	// each authorities from this set and then create object of
	// SimpleGrantedAuthority by passing name of authority inside it's constructor
	private List<GrantedAuthority> getGrantedAuthorities(Set<Authority> authorities) {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		for (Authority authority : authorities) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthorityName()));
		}
		return grantedAuthorities;
	}

}
