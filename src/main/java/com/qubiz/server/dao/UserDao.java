package com.qubiz.server.dao;

import com.qubiz.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 07.06.2018 #
 ******************************
*/
public interface UserDao extends JpaRepository<User, Integer> {

    Optional<User> findUserByUsername(String username);

}
