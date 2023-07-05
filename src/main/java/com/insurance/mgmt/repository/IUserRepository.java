package com.insurance.mgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.insurance.mgmt.entity.User;

public interface IUserRepository extends JpaRepository<User,Integer>{ //Object, id (type)

}
