package com.insurance.mgmt.service.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insurance.mgmt.entity.address.Province;
import com.insurance.mgmt.repository.address.IProvinceRepository;

@Service
public class ProvinceService {
	
	@Autowired
	IProvinceRepository provinceRepository;
	
	public List <Province> listAll(){
		return provinceRepository.findAll();
	}
}
