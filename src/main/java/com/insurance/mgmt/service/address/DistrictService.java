package com.insurance.mgmt.service.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.mgmt.entity.address.District;
import com.insurance.mgmt.repository.address.IDistrictRepository;

@Service
public class DistrictService {
	
	@Autowired
	IDistrictRepository districtRepository;
	
	public List <District> listAll(){
		return districtRepository.findAll();
	}
}
