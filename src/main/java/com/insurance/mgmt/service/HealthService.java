package com.insurance.mgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insurance.mgmt.entity.Health;
import com.insurance.mgmt.repository.IHealthRepository;

@Service
public class HealthService {
	@Autowired
	private IHealthRepository healthRepository;
	
	public void save(Health health) {
		healthRepository.save(health);
	}
	
	public List<Health> getAllHealths(){
		return healthRepository.findAll(); 
	}
	
	public Health getHealthById(int id) {
		return healthRepository.findById(id).get();
	}
	
	public void deleteById(int id) {
		healthRepository.deleteById(id);
	}
    
    public List<Health> findByStatusAndCustomerId(int status, int id) {
        return healthRepository.findByStatusAndCustomerId(status, id);
    }

	public List<Health> findByForWhoAndStatus(String forWho, int status) {
		return healthRepository.findByForWhoAndStatus(forWho, status);
	}
}
