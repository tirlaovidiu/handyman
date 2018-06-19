package com.qubiz.server;

import com.qubiz.server.dao.JobAssignmentDao;
import com.qubiz.server.dao.JobCategoryDao;
import com.qubiz.server.dao.JobDao;
import com.qubiz.server.dao.LocationDao;
import com.qubiz.server.dao.RoleDao;
import com.qubiz.server.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 07.06.2018 #
 ******************************
*/
@Component
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {


    private UserDao userDao;
    private RoleDao roleDao;
    private JobDao jobDao;
    private JobCategoryDao jobCategoryDao;
    private JobAssignmentDao jobAssignmentDao;
    private LocationDao locationDao;

    @Autowired
    public CommandLineRunner(UserDao userDao, RoleDao roleDao, JobDao jobDao, JobCategoryDao jobCategoryDao, JobAssignmentDao jobAssignmentDao, LocationDao locationDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.jobDao = jobDao;
        this.jobCategoryDao = jobCategoryDao;
        this.jobAssignmentDao = jobAssignmentDao;
        this.locationDao = locationDao;
    }


    @Override
    public void run(String... args) {

    }
}
