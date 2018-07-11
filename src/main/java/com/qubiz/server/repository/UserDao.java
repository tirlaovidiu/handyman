package com.qubiz.server.repository;

import com.qubiz.server.entity.model.Role;
import com.qubiz.server.entity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 07.06.2018 #
 ******************************
*/
public interface UserDao extends JpaRepository<User, Integer> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByUserTokenId(String userTokenId);

    List<User> findUsersByRoles(Role role);
}
