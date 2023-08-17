package com.insurance.mgmt.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insurance.mgmt.entity.Insurance;
import com.insurance.mgmt.repository.IHealthRepository;
import com.insurance.mgmt.repository.IInsuranceRepository;

@Service
public class InsuranceService {
	@Autowired
	IInsuranceRepository insuranceRepository;
	
	@Autowired
	IHealthRepository healthRepository;
	
	public void save(Insurance insurance) {
		insuranceRepository.save(insurance);
	}
	
	public List<Insurance> getAllInsurance(){
		return insuranceRepository.findAll();
	}
	
	public Insurance getInsuranceById(int id) {
		return insuranceRepository.findById(id).get();
	}
	
	public Insurance getInsuranceByHomeId(int homeId) {
        Optional<Insurance> optionalInsurance = insuranceRepository.findByHomeId(homeId);
        return optionalInsurance.orElse(null); // Eğer nesne varsa nesneyi, yoksa null dönecektir.
    }
	
	public List<Insurance> getInsurancesByHomeId(int homeId) {
	    Optional<Insurance> optionalInsurance = insuranceRepository.findByHomeId(homeId);
	    return optionalInsurance.map(Collections::singletonList).orElse(Collections.emptyList());
	}

	
	public Insurance getInsuranceByHealthId(int healthId) {
        Optional<Insurance> optionalInsurance = insuranceRepository.findByHealthId(healthId);
        return optionalInsurance.orElse(null); // Eğer nesne varsa nesneyi, yoksa null dönecektir.
    }
	
	public void deleteById(int id) {
		insuranceRepository.deleteById(id);
	}
	
	public List<Insurance> findByStatus(int status) {
        return insuranceRepository.findByStatus(status);
    }

    public Optional<Insurance> findByHomeId(int homeId) {
        return insuranceRepository.findByHomeId(homeId);
    }

    public List<Insurance> findByStatusAndHomeId(int status, int homeId) {
        return insuranceRepository.findByStatusAndHomeId(status, homeId);
    }
    
    public List<Insurance> findByStatusAndCustomerId(int status, int customerId) {
        return insuranceRepository.findByStatusAndCustomerId(status, customerId);
    }
    
    public List<Insurance> findByStatusAndCustomerIdAndResult(int status, int customerId, String result) {
        return insuranceRepository.findByStatusAndCustomerIdAndResult(status, customerId, result);
    }

    public List<Insurance> findByStatusAndResultAndHomeId(int status, String result, int homeId) {
        return insuranceRepository.findByStatusAndResultAndHomeId(status, result, homeId);
    }

	public List<Insurance> findByStatusAndHealthId(int status, int healthId) {
		return insuranceRepository.findByStatusAndHealthId(status, healthId);
	}

	public List<Insurance> findByStatusAndResultAndHealthId(int status, String result, int healthId) {
		return insuranceRepository.findByStatusAndResultAndHealthId(status, result, healthId);
	}
}
