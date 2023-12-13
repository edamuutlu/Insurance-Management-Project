package com.insurance.mgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.CarInsurance;

@Repository
public interface ICarInsuranceRepository extends JpaRepository<CarInsurance,Integer>{ 
	
	@Query("SELECT ci FROM CarInsurance ci WHERE ci.status = :status")
    List<CarInsurance> findByStatus(@Param("status") int status);

    @Query("SELECT ci FROM CarInsurance ci WHERE ci.status = :status AND ci.customerId = :customerId")
    List<CarInsurance> findByStatusAndCustomerId(@Param("status") int status, @Param("customerId") int customerId);

    @Query("SELECT ci FROM CarInsurance ci WHERE ci.status = :status AND ci.customerId = :customerId AND ci.result LIKE %:result%")
    List<CarInsurance> findByStatusAndCustomerIdAndResult(@Param("status") int status, @Param("customerId") int customerId, @Param("result") String result);

    @Query("SELECT ci FROM CarInsurance ci WHERE ci.carId = :carId")
    Optional<CarInsurance> findByCarId(@Param("carId") int carId);

    @Query("SELECT ci FROM CarInsurance ci WHERE ci.status = :status AND ci.carId IN(:carId)")
    List<CarInsurance> findByStatusAndCarId(@Param("status") int status, @Param("carId") int carId);

    @Query("SELECT ci FROM CarInsurance ci WHERE ci.status = :status AND ci.result LIKE %:result% AND ci.carId = :carId")
    List<CarInsurance> findByStatusAndResultAndCarId(@Param("status") int status, @Param("result") String result, @Param("carId") int carId);

}
