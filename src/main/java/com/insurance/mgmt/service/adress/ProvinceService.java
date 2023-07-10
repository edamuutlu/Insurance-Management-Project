package com.insurance.mgmt.service.adress;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insurance.mgmt.entity.adress.Province;
import com.insurance.mgmt.repository.adress.IProvinceRepository;

@Service
public class ProvinceService {
	
	@Autowired
	IProvinceRepository provinceRepository;
	
	public List <Province> listAll(){
		return provinceRepository.findAll();
	}
}
