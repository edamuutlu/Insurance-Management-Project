package com.insurance.mgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.HomeInsurance;

@Repository
public interface IHomeInsuranceRepository extends JpaRepository<HomeInsurance,Integer>{ 
	
	List<HomeInsurance> findByStatus(int status);
	
	List<HomeInsurance> findByStatusAndCustomerId(int status, int customerId);
	
	List<HomeInsurance> findByStatusAndCustomerIdAndResult(int status, int customerId, String result);
	
	Optional<HomeInsurance> findByHomeId(int homeId);
	
	List<HomeInsurance> findByStatusAndHomeId(int status, int homeId);
	
	List<HomeInsurance> findByStatusAndResultAndHomeId(int status, String result, int homeId);

}
