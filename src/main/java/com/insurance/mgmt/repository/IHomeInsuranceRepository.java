package com.insurance.mgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.HomeInsurance;

@Repository
public interface IHomeInsuranceRepository extends JpaRepository<HomeInsurance,Integer>{ 
	
	@Query("SELECT hi FROM HomeInsurance hi WHERE hi.status = :status")
    List<HomeInsurance> findByStatus(@Param("status") int status);

    @Query("SELECT hi FROM HomeInsurance hi WHERE hi.status = :status AND hi.customerId = :customerId")
    List<HomeInsurance> findByStatusAndCustomerId(@Param("status") int status, @Param("customerId") int customerId);

    @Query("SELECT hi FROM HomeInsurance hi WHERE hi.status = :status AND hi.customerId = :customerId AND hi.result = :result")
    List<HomeInsurance> findByStatusAndCustomerIdAndResult(@Param("status") int status, @Param("customerId") int customerId, @Param("result") String result);

    @Query("SELECT hi FROM HomeInsurance hi WHERE hi.homeId = :homeId")
    Optional<HomeInsurance> findByHomeId(@Param("homeId") int homeId);

    @Query("SELECT hi FROM HomeInsurance hi WHERE hi.status = :status AND hi.homeId = :homeId")
    List<HomeInsurance> findByStatusAndHomeId(@Param("status") int status, @Param("homeId") int homeId);

    @Query("SELECT hi FROM HomeInsurance hi WHERE hi.status = :status AND hi.result = :result AND hi.homeId = :homeId")
    List<HomeInsurance> findByStatusAndResultAndHomeId(@Param("status") int status, @Param("result") String result, @Param("homeId") int homeId);

}
