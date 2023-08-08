package com.insurance.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.Health;

@Repository
public interface IHealthRepository  extends JpaRepository<Health,Integer>{

	List<Health> findByStatus(int status);

	List<Health> findByCustomerId(int id);

	List<Health> findByStatusAndCustomerId(int status, int id);

}
