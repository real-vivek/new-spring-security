package com.real.vivek.config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.stereotype.Service;

import com.real.vivek.beans.Customer;
import com.real.vivek.repo.CustomerRepository;

//We have requirement to only get the customer so we are implementing UserDetails
//If we had requirements of CRUD operations on Customer we may extend UserDetailsManager. We can take reference of JDBC/InMemeory/LDAP UserDetailsManager to implement such functionality
//Here spring security will know that we have a sample UserDetailsService implementation as we have @Service annotation, so it will use this as the implementation and not go to the default implementation
@Service
public class MultiTableDetailsService implements UserDetailsService{
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private DataSource dataSource;
	
	//The DAOAuthenticationProvider will call userdetailsService.loadUsername so in our case the DAO Authentication will call the below method and return UserDetails object
	//The DAOAuthenticationProvider will then convert this UserDetails object to Authentication object 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//Here we handle logic to where to divert which call
		//If username contains @ then user should be fetched from our Customer table
		//Else we need to fetch it using custom JdbcDaoImpl where we have custom configuration to fetch from my_users, my_authorities
		//Here custom configuration for data source can be made to fetch user from 2 different databases
		if(username.contains("@")) {
			List<Customer> customers = customerRepository.findByEmail(username);
			if(customers.size()==0) {
				throw new UsernameNotFoundException("User details not found for the user : "+ username);
			}
			return new SecurityCustomer(customers.get(0));
		}
		else{
			UserDetailsService defaultInMemoryUserDetailsService = jdbcDaoImpl(dataSource);			
			UserDetails inMemoryUser = defaultInMemoryUserDetailsService.loadUserByUsername(username);
			return inMemoryUser;
		}
	}
	
	 public JdbcDaoImpl jdbcDaoImpl(DataSource dataSource) {
		JdbcDaoImpl jdbcDaoImpl = new JdbcDaoImpl();
		jdbcDaoImpl.setDataSource(dataSource);
		jdbcDaoImpl.setUsersByUsernameQuery("select uname,pass,enabled from my_users where uname = ?");
		jdbcDaoImpl.setAuthoritiesByUsernameQuery("select uname,authority from my_authorities where uname = ?");
		return jdbcDaoImpl;
	}

}
