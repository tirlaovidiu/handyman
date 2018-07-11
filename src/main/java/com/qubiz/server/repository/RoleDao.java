package com.qubiz.server.repository;

import com.qubiz.server.entity.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 19.06.2018 #
 ******************************
*/
public interface RoleDao extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByRoleName(String roleName);
}
