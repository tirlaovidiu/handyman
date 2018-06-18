package com.qubiz.server.dao;

import com.qubiz.server.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 18.06.2018 #
 ******************************
*/
public interface RoleDao extends JpaRepository<Role, Integer> {

    Optional<Role> findRoleByRoleName(String roleName);
}
