package com.insurance.mgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.insurance.mgmt.entity.Customer;

public interface ICustomerRepository extends JpaRepository<Customer,Integer>{ //Object, id (type)

}
