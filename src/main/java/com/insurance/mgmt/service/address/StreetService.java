package com.insurance.mgmt.service.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.mgmt.entity.address.Street;
import com.insurance.mgmt.repository.address.IStreetRepository;

@Service
public class StreetService {
	@Autowired
	IStreetRepository streetRepository;
	
	public List <Street> listAll(){
		return streetRepository.findAll();
	}
}
