package com.insurance.mgmt.service.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.mgmt.entity.address.Neighbourhood;
import com.insurance.mgmt.repository.address.INeighbourhoodRepository;

@Service
public class NeighbourhoodService {
	@Autowired
	INeighbourhoodRepository neighbourhoodRepository;
	
	public List <Neighbourhood> listAll(){
		return neighbourhoodRepository.findAll();
	}
}
