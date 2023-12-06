package com.insurance.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insurance.mgmt.entity.Health;

@Repository
public interface IHealthRepository  extends JpaRepository<Health,Integer>{

	@Query("SELECT h FROM Health h WHERE h.status = :status AND h.customerId = :id")
    List<Health> findByStatusAndCustomerId(@Param("status") int status, @Param("id") int id);

    @Query("SELECT h FROM Health h WHERE h.forWho = :forWho AND h.status = :status")
    List<Health> findByForWhoAndStatus(@Param("forWho") String forWho, @Param("status") int status);

}
