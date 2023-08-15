package com.insurance.mgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.OfferKdv;

@Repository
public interface IOfferKdvRepository extends JpaRepository<OfferKdv,Integer> {

	OfferKdv findByLastCustomerId(int lastCustomerId);
}
