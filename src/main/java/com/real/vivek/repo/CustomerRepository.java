package com.real.vivek.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.real.vivek.beans.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer>{

	List<Customer> findByEmail(String email);
	
}
