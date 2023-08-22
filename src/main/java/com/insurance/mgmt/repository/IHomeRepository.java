package com.insurance.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.Home;

@Repository
public interface IHomeRepository extends JpaRepository<Home,Integer>{ 
	
	List<Home> findByStatusAndCustomerId(int status, int customerId);
	
	List<Home> findByProvinceAndDistrictAndNeighbourhoodAndBuildingNumberAndApartmentAndFloorAndStatus(
            String province, String district, String neighbourhood,
            int buildingNumber, int apartment, String floor, int status);
}
