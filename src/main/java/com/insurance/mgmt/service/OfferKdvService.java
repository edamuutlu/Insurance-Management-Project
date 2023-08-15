package com.insurance.mgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insurance.mgmt.entity.OfferKdv;
import com.insurance.mgmt.repository.IOfferKdvRepository;

@Service
public class OfferKdvService {
	@Autowired
	IOfferKdvRepository oldKdvRepository;
	
	public OfferKdv findByLastCustomerId(int lastCustomerId) {
		return oldKdvRepository.findByLastCustomerId(lastCustomerId);
	}
	
	public void save(OfferKdv oldKdv) {
		oldKdvRepository.save(oldKdv);
	}
}
