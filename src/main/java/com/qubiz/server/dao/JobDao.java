package com.qubiz.server.dao;

import com.qubiz.server.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 12.06.2018 #
 ******************************
*/
public interface JobDao extends JpaRepository<Job,Integer> {
}
