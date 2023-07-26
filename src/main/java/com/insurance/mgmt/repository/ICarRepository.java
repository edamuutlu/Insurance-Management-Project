package com.insurance.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.insurance.mgmt.entity.Car;

public interface ICarRepository extends JpaRepository<Car, Integer>{
	List<Car> findByStatus(int status);
	 List<Car> findByStatusAndCustomerId(int status, int customerId);
	 List<Car> findByStatusAndPlate1AndPlate2AndPlate3AndResult(int status, int plate1, String plate2, int plate3, String result);
	 List<Car> findByStatusAndCustomerIdAndResult(int status,int customerId, String result);
}
