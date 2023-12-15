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
    
    @Query(value = "SELECT DISTINCT insurance_type "
    		+ "FROM ("
    		+ "    SELECT *, 'car' AS insurance_type1 "
    		+ "    FROM car_insurance"
    		+ "    UNION ALL"
    		+ "    SELECT *, 'home' AS insurance_type2 "
    		+ "    FROM home_insurance"
    		+ "    UNION ALL"
    		+ "    SELECT *, 'health' AS insurance_type3 "
    		+ "    FROM health_insurance "
    		+ ") AS all_insurances", nativeQuery = true)
    List<String> getAllProductType();
    
    @Query(value = "SELECT comp.company_id, comp.name, COALESCE(SUM(offer - refund), 0) AS carBalance FROM car_insurance car "
    		+ "RIGHT JOIN company comp ON car.company_id = comp.company_id "
    		+ "GROUP BY car.company_id,  comp.company_id ", nativeQuery = true)
    List<Object[]> getCarInsuranceBalance();
    
    @Query(value = "SELECT comp.company_id, comp.name, COALESCE(SUM(offer - refund), 0) AS homeBalance FROM home_insurance home "
    		+ "RIGHT JOIN company comp ON home.company_id = comp.company_id "
    		+ "GROUP BY home.company_id,  comp.company_id ", nativeQuery = true)
    List<Object[]> getHomeInsuranceBalance();
    
    @Query(value = "SELECT comp.company_id, comp.name, COALESCE(SUM(offer - refund), 0) AS healthBalance FROM health_insurance health "
    		+ "RIGHT JOIN company comp ON health.company_id = comp.company_id "
    		+ "GROUP BY health.company_id,  comp.company_id ", nativeQuery = true)
    List<Object[]> getHealthInsuranceBalance();

}