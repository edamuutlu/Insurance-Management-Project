package com.insurance.mgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.mgmt.entity.Home;
import com.insurance.mgmt.repository.IHomeRepository;

@Service
public class HomeService {
	@Autowired
	private IHomeRepository homeRepository;
	
	public void save(Home home) {
		homeRepository.save(home);
	}
	
	public List<Home> getAllHomes(){
		return homeRepository.findAll();
	}
	
	public Home getHomeById(int id) {
		return homeRepository.findById(id).get();
	}
	
	public void deleteById(int id) {
		homeRepository.deleteById(id);
	}
}
