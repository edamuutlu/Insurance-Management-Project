package com.insurance.mgmt.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.insurance.mgmt.entity.Insurance;

public interface IInsuranceRepository extends JpaRepository<Insurance,Integer>{ //Object, id (type)
	List<Insurance> findByStatus(int status);
}
