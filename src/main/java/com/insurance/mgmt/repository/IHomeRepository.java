package com.insurance.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.Home;

@Repository
public interface IHomeRepository extends JpaRepository<Home,Integer>{ 
	
	@Query("SELECT h FROM Home h WHERE h.status = :status AND h.customerId = :customerId")
    List<Home> findByStatusAndCustomerId(@Param("status") int status, @Param("customerId") int customerId);

    @Query("SELECT h FROM Home h WHERE h.province = :province AND h.district = :district AND h.neighbourhood = :neighbourhood AND h.buildingNumber = :buildingNumber AND h.apartment = :apartment AND h.floor = :floor AND h.status = :status")
    List<Home> findByProvinceAndDistrictAndNeighbourhoodAndBuildingNumberAndApartmentAndFloorAndStatus(
            @Param("province") String province, @Param("district") String district, @Param("neighbourhood") String neighbourhood,
            @Param("buildingNumber") int buildingNumber, @Param("apartment") int apartment, @Param("floor") String floor, @Param("status") int status);
}
