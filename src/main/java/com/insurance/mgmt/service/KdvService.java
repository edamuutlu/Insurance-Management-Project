package com.insurance.mgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.mgmt.entity.Kdv;
import com.insurance.mgmt.repository.IKdvRepository;

@Service
public class KdvService {
	@Autowired
	private IKdvRepository kdvRepository;
	
	public Kdv getProductTypeById(int productType) {
		return kdvRepository.findById(productType).get();
	}
	
	public void save(Kdv kdv) {
		kdvRepository.save(kdv);
	}
}
