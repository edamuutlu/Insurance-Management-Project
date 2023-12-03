package com.insurance.mgmt.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insurance.mgmt.entity.HomeInsurance;
import com.insurance.mgmt.repository.IHomeInsuranceRepository;

@Service
public class HomeInsuranceService {
	@Autowired
	IHomeInsuranceRepository homeInsuranceRepository;
	
	public void save(HomeInsurance insurance) {
		homeInsuranceRepository.save(insurance);
	}
	
	public List<HomeInsurance> getAllInsurance(){
		return homeInsuranceRepository.findAll();
	}
	
	public HomeInsurance getInsuranceById(int id) {
		return homeInsuranceRepository.findById(id).get();
	}
	
	public HomeInsurance getInsuranceByHomeId(int homeId) {
        Optional<HomeInsurance> optionalInsurance = homeInsuranceRepository.findByHomeId(homeId);
        return optionalInsurance.orElse(null); // Eğer nesne varsa nesneyi, yoksa null dönecektir.
    }
	
	public List<HomeInsurance> getInsurancesByHomeId(int homeId) {
	    Optional<HomeInsurance> optionalInsurance = homeInsuranceRepository.findByHomeId(homeId);
	    return optionalInsurance.map(Collections::singletonList).orElse(Collections.emptyList());
	}
	
	public void deleteById(int id) {
		homeInsuranceRepository.deleteById(id);
	}
	
	public List<HomeInsurance> findByStatus(int status) {
        return homeInsuranceRepository.findByStatus(status);
    }
	
	public Optional<HomeInsurance> findByHomeId(int homeId) {
        return homeInsuranceRepository.findByHomeId(homeId);
    }

    public List<HomeInsurance> findByStatusAndHomeId(int status, int homeId) {
        return homeInsuranceRepository.findByStatusAndHomeId(status, homeId);
    }
    
    public List<HomeInsurance> findByStatusAndCustomerId(int status, int customerId) {
        return homeInsuranceRepository.findByStatusAndCustomerId(status, customerId);
    }
    
    public List<HomeInsurance> findByStatusAndCustomerIdAndResult(int status, int customerId, String result) {
        return homeInsuranceRepository.findByStatusAndCustomerIdAndResult(status, customerId, result);
    }
    
    public List<HomeInsurance> findByStatusAndResultAndHomeId(int status, String result, int homeId) {
        return homeInsuranceRepository.findByStatusAndResultAndHomeId(status, result, homeId);
    }
}
