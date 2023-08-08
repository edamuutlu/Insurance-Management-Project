package com.insurance.mgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.Insurance;

@Repository
public interface IInsuranceRepository extends JpaRepository<Insurance,Integer>{ //Object, id (type)
	List<Insurance> findByStatus(int status);
	Optional<Insurance> findByHomeId(int homeId);  
	Optional<Insurance> findByHealthId(int healthId);
	List<Insurance> findByStatusAndHomeId(int status, int homeId);
	List<Insurance> findByStatusAndCustomerId(int status, int customerId);
	List<Insurance> findByStatusAndResultAndHomeId(int status, String result, int homeId);
	List<Insurance> findByStatusAndHealthId(int status, int healthId);
}
