package com.insurance.mgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.HealthInsurance;

@Repository
public interface IHealthInsuranceRepository extends JpaRepository<HealthInsurance,Integer>{ 
	
	List<HealthInsurance> findByStatus(int status);
	
	List<HealthInsurance> findByStatusAndCustomerId(int status, int customerId);
	
	List<HealthInsurance> findByStatusAndCustomerIdAndResult(int status, int customerId, String result);
	
	Optional<HealthInsurance> findByHealthId(int healthId);
	
	List<HealthInsurance> findByStatusAndHealthId(int status, int healthId);
	
	List<HealthInsurance> findByStatusAndResultAndHealthId(int status, String result, int healthId);

}
