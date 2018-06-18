package com.qubiz.server;

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


    private UserDao userDao;
    private JobDao jobDao;
    private JobCategoryDao jobCategoryDao;
    private JobAssignmentDao jobAssignmentDao;
    private LocationDao locationDao;

    @Autowired
    public CommandLineRunner(UserDao userDao, JobDao jobDao, JobCategoryDao jobCategoryDao, JobAssignmentDao jobAssignmentDao, LocationDao locationDao) {
        this.userDao = userDao;
        this.jobDao = jobDao;
        this.jobCategoryDao = jobCategoryDao;
        this.jobAssignmentDao = jobAssignmentDao;
        this.locationDao = locationDao;
    }


    @Override
    public void run(String... args) {
//        Client client = new Client();
//        client.setUsername("username2");
//        client.setFirstName("firstName2");
//        client.setLastName("lastName2");
//        client.setPassword("password2");
//        client.setPaymentOption("paymentOption2");
//        clientDao.save(client);
//        Optional<Expert> expert = expertDao.findById(2);
//        expert.ifPresent(client -> {
//            client.setDescription("description");
//            expertDao.save(client);
//
//        });
//        Optional<User> user = userDao.findById(2);
//        user.ifPresent(user1 -> {
//            Expert expert = new Expert();
//            expert.setId(user1.getId());
//            expert.setFirstName(user1.getFirstName());
//            expert.setLastName(user1.getLastName());
//            expert.setUsername(user1.getUsername());
//            expert.setPassword(user1.getPassword());
//            expert.setDescription("dsadsa");
//
//            expertDao.save(expert);
//        });

    }
}
