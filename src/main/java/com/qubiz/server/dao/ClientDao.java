package com.qubiz.server.dao;

import com.qubiz.server.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 12.06.2018 #
 ******************************
*/
public interface ClientDao extends JpaRepository<Client, Integer> {

    Optional<Client> findClientByUsername(String username);

}
