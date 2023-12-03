package com.insurance.mgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.CarInsurance;

@Repository
public interface ICarInsuranceRepository extends JpaRepository<CarInsurance,Integer>{ 
	
	List<CarInsurance> findByStatus(int status);
	
	List<CarInsurance> findByStatusAndCustomerId(int status, int customerId);
	
	List<CarInsurance> findByStatusAndCustomerIdAndResult(int status, int customerId, String result);
	
	Optional<CarInsurance> findByCarId(int carId); 
	
	List<CarInsurance> findByStatusAndCarId(int status, int carId);
	
	List<CarInsurance> findByStatusAndResultAndCarId(int status, String result, int carId);

}
