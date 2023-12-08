package com.insurance.mgmt.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.insurance.mgmt.entity.CarInsurance;

import java.util.List;

public interface IInsuranceRepository extends Repository<CarInsurance, Long> {

	@Query(value = "SELECT * FROM car_insurance "
            + "UNION ALL "
            + "SELECT * FROM home_insurance "
            + "UNION ALL "
            + "SELECT * FROM health_insurance", nativeQuery = true)
    List<Object[]> getUnionAllResult();
    
    
}
/*package com.insurance.mgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.Insurance;

@Repository
public interface IInsuranceRepository extends JpaRepository<Insurance,Integer>{ 
	
	//List<Insurance> findByStatus(int status);
	
	Optional<Insurance> findByHomeId(int homeId); 
	
	Optional<Insurance> findByCarId(int carId); 
	
	Optional<Insurance> findByHealthId(int healthId);
	
	List<Insurance> findByStatusAndHomeId(int status, int homeId);
	
	List<Insurance> findByStatusAndCarId(int status, int carId);
	
	//List<Insurance> findByStatusAndCustomerId(int status, int customerId);
	
	List<Insurance> findByStatusAndHealthId(int status, int healthId);
	
	List<Insurance> findByStatusAndResultAndHomeId(int status, String result, int homeId);
	
	List<Insurance> findByStatusAndResultAndCarId(int status, String result, int carId);
	
	List<Insurance> findByStatusAndResultAndHealthId(int status, String result, int healthId);
	
	//List<Insurance> findByStatusAndCustomerIdAndResult(int status, int customerId, String result);
	 
} */
