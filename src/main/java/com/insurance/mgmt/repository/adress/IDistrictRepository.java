package com.insurance.mgmt.repository.adress;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.adress.District;

@Repository
public interface IDistrictRepository extends JpaRepository<District, Integer>{
}
