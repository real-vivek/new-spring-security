package com.real.vivek.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.real.vivek.beans.Customer;
import com.real.vivek.repo.CustomerRepository;

//We have requirement to only get the customer so we are implementing UserDetails
//If we had requirements of CRUD operations on Customer we may extend UserDetailsManager. We can take reference of JDBC/InMemeory/LDAP UserDetailsManager to implement such functionality
//Here spring security will know that we have a sample UserDetailsService implementation as we have @Service annotation, so it will use this as the implementation and not go to the default implementation
@Service
public class CustomerDetailsService implements UserDetailsService{
	
	@Autowired
	private CustomerRepository customerRepository; 
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<Customer> customers = customerRepository.findByEmail(username);
		if(customers.size()==0) {
			throw new UsernameNotFoundException("User details not found for the user : "+ username);
		}
		return new SecurityCustomer(customers.get(0));
	}

}
