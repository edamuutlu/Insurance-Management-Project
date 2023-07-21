package com.insurance.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.insurance.mgmt.entity.Home;

public interface IHomeRepository extends JpaRepository<Home,Integer>{ //Object, id (type)
	List<Home> findByStatus(int status);
}
