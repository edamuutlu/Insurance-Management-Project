package com.insurance.mgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.insurance.mgmt.entity.Insurance;

public interface IInsuranceRepository extends JpaRepository<Insurance,Integer>{ //Object, id (type)
	List<Insurance> findByStatus(int status);
	Optional<Insurance> findByHomeId(int homeId); 
	List<Insurance> findByStatusAndHomeId(int status, int homeId);
	//Optional<Insurance> findByCustomerId(int customerId);
	List<Insurance> findByStatusAndResultAndHomeId(int status, String result, int homeId);
}
