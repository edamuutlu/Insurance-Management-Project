package com.insurance.mgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.insurance.mgmt.entity.Car;

public interface ICarRepository extends JpaRepository<Car, Integer>{

}
