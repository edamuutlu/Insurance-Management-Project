package com.insurance.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.insurance.mgmt.entity.Kdv;

@Repository
public interface IKdvRepository extends JpaRepository<Kdv,Integer> {
	List<Kdv> findByKdvId(int kdvId);
}
