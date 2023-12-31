package com.insurance.mgmt.service;

import java.util.List;

//import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.mgmt.entity.Car;
import com.insurance.mgmt.repository.ICarRepository;

@Service
public class CarService {
	@Autowired
	private ICarRepository carRepository;
	
	public void save(Car car) {
		carRepository.save(car);
	}
	
	public List<Car> getAllCars(){
		return carRepository.findAll();
	}
	
	public void deleteById(int id) {
		carRepository.deleteById(id);
	}
	
	public Car getCarId(int id) {
		return carRepository.findById(id).get();
	}
	
    public List<Car> findByStatusAndCustomerId(int status, int customerId) {
        return carRepository.findByStatusAndCustomerId(status, customerId);
    }

    public List<Car> findByStatusAndPlate1AndPlate2AndPlate3(int status, int plate1, String plate2, int plate3) {
        return carRepository.findByStatusAndPlate1AndPlate2AndPlate3(status, plate1, plate2, plate3);
    }

}
