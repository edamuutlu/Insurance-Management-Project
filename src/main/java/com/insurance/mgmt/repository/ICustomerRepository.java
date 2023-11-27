package com.insurance.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.Customer;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer,Integer>{ 
	
	//Nuri1231231231
	//Eda123123123
	@Query("FROM Customer WHERE status=?1")
	List<Customer> findByStatus(int status);
	
	@Query("FROM Customer WHERE status=?1 and tc=?2")
	List<Customer> findByStatusAndTc(int status, String tc);
	
	List<Customer> findByStatusAndEmail(int status, String email);
}
