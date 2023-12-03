package com.insurance.mgmt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insurance.mgmt.entity.CarInsurance;
import com.insurance.mgmt.repository.ICarInsuranceRepository;

@Service
public class CarInsuranceService {
	@Autowired
	ICarInsuranceRepository carInsuranceRepository;
	
	public void save(CarInsurance insurance) {
		carInsuranceRepository.save(insurance);
	}
	
	public List<CarInsurance> getAllInsurance(){
		return carInsuranceRepository.findAll();
	}
	
	public CarInsurance getInsuranceById(int id) {
		return carInsuranceRepository.findById(id).get();
	}
	
	public CarInsurance getInsuranceByCarId(int carId) {
        Optional<CarInsurance> optionalInsurance = carInsuranceRepository.findByCarId(carId);
        return optionalInsurance.orElse(null); // Eğer nesne varsa nesneyi, yoksa null dönecektir.
    }
	
	public void deleteById(int id) {
		carInsuranceRepository.deleteById(id);
	}
	
	public List<CarInsurance> findByStatus(int status) {
        return carInsuranceRepository.findByStatus(status);
    }
	
	public List<CarInsurance> findByStatusAndCarId(int status, int carId) {
        return carInsuranceRepository.findByStatusAndCarId(status, carId);
    }
	
	public List<CarInsurance> findByStatusAndCustomerId(int status, int customerId) {
        return carInsuranceRepository.findByStatusAndCustomerId(status, customerId);
    }
	
	public List<CarInsurance> findByStatusAndCustomerIdAndResult(int status, int customerId, String result) {
        return carInsuranceRepository.findByStatusAndCustomerIdAndResult(status, customerId, result);
    }
	
	public List<CarInsurance> findByStatusAndResultAndCarId(int status, String result, int carId) {
        return carInsuranceRepository.findByStatusAndResultAndCarId(status, result, carId);
    }
}
