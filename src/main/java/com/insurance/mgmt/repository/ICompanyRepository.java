package com.insurance.mgmt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.Company;

@Repository
public interface ICompanyRepository extends JpaRepository<Company,Integer>{
	
	@Query("SELECT c FROM Company c WHERE c.companyId = :companyId")
    Optional<Company> findByCompanyId(@Param("companyId") int companyId);
}
