package com.insurance.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.Car;

@Repository
public interface ICarRepository extends JpaRepository<Car, Integer>{
	
	 List<Car> findByStatusAndCustomerId(int status, int customerId);
	 
	 List<Car> findByStatusAndPlate1AndPlate2AndPlate3(int status, int plate1, String plate2, int plate3);
	 
}
