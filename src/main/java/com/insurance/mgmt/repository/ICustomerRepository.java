package com.insurance.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.Customer;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer,Integer>{ //Object, id (type)
	List<Customer> findByStatus(int status);
	List<Customer> findByStatusAndTc(int status, String tc);
	List<Customer> findByStatusAndEmail(int status, String email);
}
