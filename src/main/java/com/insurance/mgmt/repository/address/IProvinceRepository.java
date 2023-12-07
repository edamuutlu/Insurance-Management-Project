package com.insurance.mgmt.repository.address;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.address.Province;

@Repository
public interface IProvinceRepository extends JpaRepository<Province, Integer>{
	
	@Query("SELECT p FROM Province p WHERE p.id = :id")
    Optional<Province> findById(@Param("id") int id);
}
