package com.real.vivek.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.real.vivek.beans.Customer;

// This is the custom schema that we have created which will be used for authentication
public class SecurityCustomer implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	private Customer customer;
	
	//Here we are injecting customer bean using constructor injection
	public SecurityCustomer(Customer customer) {
		this.customer=customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(customer.getRole()));
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return customer.getPwd();
	}

	//Here instead of username we have returned email as we want to do authentication witl email address
	@Override
	public String getUsername() {
		return customer.getEmail();
	}

	//We are not implementing the functionality for below values so returning true values
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
