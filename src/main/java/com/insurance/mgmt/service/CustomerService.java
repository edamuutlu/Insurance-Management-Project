package com.insurance.mgmt.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.mgmt.entity.Customer;
import com.insurance.mgmt.repository.ICustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	ICustomerRepository customerRepository;
	
	public void save(Customer customer) {
		customerRepository.save(customer);
	}
	
	public List<Customer> getAllCustomer(){
		return customerRepository.findAll();
	}
	
//	public Customer getBookById(int id) {
//		return customerRepository.findById(id).get();
//	}
//	
	public void deleteById(int id) {
		customerRepository.deleteById(id);
	}
}
