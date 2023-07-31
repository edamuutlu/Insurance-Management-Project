package com.insurance.mgmt.repository.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.insurance.mgmt.entity.address.Neighbourhood;

@Repository
public interface INeighbourhoodRepository extends JpaRepository<Neighbourhood, Integer>{

}
