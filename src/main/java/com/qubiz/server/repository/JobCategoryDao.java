package com.qubiz.server.repository;

import com.qubiz.server.entity.model.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 12.06.2018 #
 ******************************
*/
public interface JobCategoryDao extends JpaRepository<JobCategory, Integer> {
}
