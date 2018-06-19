package com.qubiz.server.dao;

import com.qubiz.server.entity.JobAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 12.06.2018 #
 ******************************
*/
public interface JobAssignmentDao extends JpaRepository<JobAssignment, Integer> {

}
