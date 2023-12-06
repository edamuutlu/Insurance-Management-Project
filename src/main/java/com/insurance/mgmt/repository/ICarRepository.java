package com.insurance.mgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insurance.mgmt.entity.Car;

import java.util.List;

public interface ICarRepository extends JpaRepository<Car, Integer> {
	
	@Query("SELECT c FROM Car c WHERE c.status = :status AND c.customerId = :customerId")
	List<Car> findByStatusAndCustomerId(@Param("status") int status, @Param("customerId") int customerId);

	@Query("SELECT c FROM Car c WHERE c.status = :status AND c.plate1 = :plate1 AND c.plate2 = :plate2 AND c.plate3 = :plate3")
	List<Car> findByStatusAndPlate1AndPlate2AndPlate3(@Param("status") int status, @Param("plate1") int plate1, @Param("plate2") String plate2, @Param("plate3") int plate3);
}
