package com.insurance.mgmt.service;

import java.util.List;
import java.util.Optional;

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
	
	public Customer getCustomerById(int customer_id) {
		return customerRepository.findById(customer_id).get();
	}
	
	public void deleteById(int id) {
		customerRepository.deleteById(id);
	}
	
	public List<Customer> findByStatus(int status) {
        return customerRepository.findByStatus(status);
    }

    public List<Customer> findByStatusAndTc(int status, String tc) {
        return customerRepository.findByStatusAndTc(status, tc);
    }

    public List<Customer> findByStatusAndEmail(int status, String email) {
        return customerRepository.findByStatusAndEmail(status, email);
    }
    
    public List<Customer> findByStatusAndUsername(int status, String username) {
        return customerRepository.findByStatusAndUsername(status, username);
    }
    
    public Customer findByUsername(String username) {
	    Optional<Customer> customerOptional = customerRepository.findByUsername(username);
	    return customerOptional.orElse(null); // Bu, eğer customerOptional içinde bir değer yoksa null döndürecektir.
	}
    
    public List<Customer> getCustomersWithIdGreaterThanOne(int status) {
        return customerRepository.findByStatusAndCustomerIdGreaterThan(status);
    }
}
