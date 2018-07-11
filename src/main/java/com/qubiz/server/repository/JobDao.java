package com.qubiz.server.repository;

import com.qubiz.server.entity.JobStatus;
import com.qubiz.server.entity.model.Job;
import com.qubiz.server.entity.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 12.06.2018 #
 ******************************
*/
public interface JobDao extends JpaRepository<Job, Integer> {

    List<Job> findJobsByClient(User client);

    Page<Job> findAllJobsByLocation_LatitudeBetweenAndLocation_LongitudeBetweenAndJobStatus(Pageable pageable, Double locationLatitudeMin, Double locationLatitudeMax, Double locationLongitudeMin, Double locationLongitudeMax, JobStatus status);
}
