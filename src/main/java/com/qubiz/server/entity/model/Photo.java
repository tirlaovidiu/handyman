package com.qubiz.server.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 22.07.2018 #
 ******************************
*/
@Entity
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(unique = true, nullable = false)
    private String awsUrl;

    private String fileName;

    public Photo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAwsUrl() {
        return awsUrl;
    }

    public void setAwsUrl(String awsUrl) {
        this.awsUrl = awsUrl;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
