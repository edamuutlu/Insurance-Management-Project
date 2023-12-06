package com.insurance.mgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.HealthInsurance;

@Repository
public interface IHealthInsuranceRepository extends JpaRepository<HealthInsurance,Integer>{ 
	
	@Query("SELECT hi FROM HealthInsurance hi WHERE hi.status = :status")
    List<HealthInsurance> findByStatus(@Param("status") int status);

    @Query("SELECT hi FROM HealthInsurance hi WHERE hi.status = :status AND hi.customerId = :customerId")
    List<HealthInsurance> findByStatusAndCustomerId(@Param("status") int status, @Param("customerId") int customerId);

    @Query("SELECT hi FROM HealthInsurance hi WHERE hi.status = :status AND hi.customerId = :customerId AND hi.result = :result")
    List<HealthInsurance> findByStatusAndCustomerIdAndResult(@Param("status") int status, @Param("customerId") int customerId, @Param("result") String result);

    @Query("SELECT hi FROM HealthInsurance hi WHERE hi.healthId = :healthId")
    Optional<HealthInsurance> findByHealthId(@Param("healthId") int healthId);

    @Query("SELECT hi FROM HealthInsurance hi WHERE hi.status = :status AND hi.healthId = :healthId")
    List<HealthInsurance> findByStatusAndHealthId(@Param("status") int status, @Param("healthId") int healthId);

    @Query("SELECT hi FROM HealthInsurance hi WHERE hi.status = :status AND hi.result = :result AND hi.healthId = :healthId")
    List<HealthInsurance> findByStatusAndResultAndHealthId(@Param("status") int status, @Param("result") String result, @Param("healthId") int healthId);

}
