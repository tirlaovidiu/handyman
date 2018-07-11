package com.qubiz.server.repository;

import com.qubiz.server.entity.model.Job;
import com.qubiz.server.entity.model.JobAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 12.06.2018 #
 ******************************
*/
public interface JobAssignmentDao extends JpaRepository<JobAssignment, Integer> {
    List<JobAssignment> findAllByJob(Job job);
}
