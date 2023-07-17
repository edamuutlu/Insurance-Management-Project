package com.insurance.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.insurance.mgmt.entity.Car;

public interface ICarRepository extends JpaRepository<Car, Integer>{
	List<Car> findByStatus(int status);
}
