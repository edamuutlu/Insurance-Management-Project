package com.insurance.mgmt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insurance.mgmt.entity.Company;
import com.insurance.mgmt.repository.ICompanyRepository;

@Service
public class CompanyService {
	@Autowired
	private ICompanyRepository companyRepository;
	
	public Company findByCompanyId(int companyId) {
        Optional<Company> optionalCompany = companyRepository.findByCompanyId(companyId);
        return optionalCompany.orElse(null); // Eğer nesne varsa nesneyi, yoksa null dönecektir.
    }
	
	public List<Company> getAllCompany(){
		return companyRepository.findAll();
	}
}
