package com.qubiz.server;

import com.qubiz.server.dao.ClientDao;
import com.qubiz.server.dao.ExpertDao;
import com.qubiz.server.dao.JobAssignmentDao;
import com.qubiz.server.dao.JobCategoryDao;
import com.qubiz.server.dao.JobDao;
import com.qubiz.server.dao.LocationDao;
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


    private final UserDao userDao;
    private final ClientDao clientDao;
    private final ExpertDao expertDao;
    private final JobDao jobDao;
    private final JobCategoryDao jobCategoryDao;
    private final JobAssignmentDao jobAssignmentDao;
    private final LocationDao locationDao;

    @Autowired
    public CommandLineRunner(UserDao userDao, ClientDao clientDao, ExpertDao expertDao, JobDao jobDao, JobCategoryDao jobCategoryDao, JobAssignmentDao jobAssignmentDao, LocationDao locationDao) {
        this.userDao = userDao;
        this.clientDao = clientDao;
        this.expertDao = expertDao;
        this.jobDao = jobDao;
        this.jobCategoryDao = jobCategoryDao;
        this.jobAssignmentDao = jobAssignmentDao;
        this.locationDao = locationDao;
    }


    @Override
    public void run(String... args) {
//        Client client = new Client();
//        client.setUsername("username");
//        client.setFirstName("firstName");
//        client.setLastName("lastName");
//        client.setPassword("password");
//        client.setPaymentOption("paymentOption");
//        clientDao.save(client);
    }
}
