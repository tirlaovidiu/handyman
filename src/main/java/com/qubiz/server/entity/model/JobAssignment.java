package com.qubiz.server.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qubiz.server.entity.JobAssignmentStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Calendar;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 07.06.2018 #
 ******************************
*/
@Entity
public class JobAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private long timeStamp = Calendar.getInstance().getTimeInMillis();

    private double price;

    @OneToOne
    private User expert;

    private JobAssignmentStatus assignmentStatus;

    @ManyToOne
    @JsonIgnore
    private Job job;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getExpert() {
        return expert;
    }

    public void setExpert(User expert) {
        this.expert = expert;
    }

    public JobAssignmentStatus getAssignmentStatus() {
        return assignmentStatus;
    }

    public void setAssignmentStatus(JobAssignmentStatus assignmentStatus) {
        this.assignmentStatus = assignmentStatus;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
