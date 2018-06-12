package com.qubiz.server.entity;

import com.qubiz.server.util.JobStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 07.06.2018 #
 ******************************
*/
@Entity
public class JobAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private double price;

    @OneToOne
    private Expert expert;

    private JobStatus status;

    @ManyToOne
    private Job job;


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Expert getExpert() {
        return expert;
    }

    public void setExpert(Expert expert) {
        this.expert = expert;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }
}
