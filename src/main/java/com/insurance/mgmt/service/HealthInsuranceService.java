package com.insurance.mgmt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insurance.mgmt.entity.HealthInsurance;
import com.insurance.mgmt.repository.IHealthInsuranceRepository;

@Service
public class HealthInsuranceService {
	@Autowired
	IHealthInsuranceRepository healthInsuranceRepository;
	
	public void save(HealthInsurance insurance) {
		healthInsuranceRepository.save(insurance);
	}
	
	public List<HealthInsurance> getAllInsurance(){
		return healthInsuranceRepository.findAll();
	}
	
	public HealthInsurance getInsuranceById(int id) {
		return healthInsuranceRepository.findById(id).get();
	}
	
	public HealthInsurance getInsuranceByHealthId(int healthId) {
        Optional<HealthInsurance> optionalInsurance = healthInsuranceRepository.findByHealthId(healthId);
        return optionalInsurance.orElse(null); // Eğer nesne varsa nesneyi, yoksa null dönecektir.
    }
	
	public void deleteById(int id) {
		healthInsuranceRepository.deleteById(id);
	}
	
	public List<HealthInsurance> findByStatus(int status) {
        return healthInsuranceRepository.findByStatus(status);
    }
	
	public List<HealthInsurance> findByStatusAndCustomerId(int status, int customerId) {
        return healthInsuranceRepository.findByStatusAndCustomerId(status, customerId);
    }
	
	public List<HealthInsurance> findByStatusAndCustomerIdAndResult(int status, int customerId, String result) {
        return healthInsuranceRepository.findByStatusAndCustomerIdAndResult(status, customerId, result);
    }
	
	public List<HealthInsurance> findByStatusAndHealthId(int status, int healthId) {
		return healthInsuranceRepository.findByStatusAndHealthId(status, healthId);
	}
	
	public List<HealthInsurance> findByStatusAndResultAndHealthId(int status, String result, int healthId) {
		return healthInsuranceRepository.findByStatusAndResultAndHealthId(status, result, healthId);
	}
}
