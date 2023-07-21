package com.insurance.mgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insurance.mgmt.entity.Insurance;
import com.insurance.mgmt.repository.IInsuranceRepository;

@Service
public class InsuranceService {
	@Autowired
	IInsuranceRepository insuranceRepository;
	
	public void save(Insurance insurance) {
		insuranceRepository.save(insurance);
	}
	
	public List<Insurance> getAllInsurance(){
		return insuranceRepository.findAll();
	}
	
	public Insurance getInsuranceById(int id) {
		return insuranceRepository.findById(id).get();
	}
	
	public void deleteById(int id) {
		insuranceRepository.deleteById(id);
	}
}
