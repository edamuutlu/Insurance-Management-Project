package com.insurance.mgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.Jobs;

@Repository
public interface IJobsRepository extends JpaRepository<Jobs, String> {
 
}
