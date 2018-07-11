package com.qubiz.server.entity.dto.response;

import com.qubiz.server.entity.JobAssignmentStatus;
import com.qubiz.server.entity.dto.UserProfileDto;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 06.07.2018 #
 ******************************
*/
public class JobAssignmentResponse {
    private int id;
    private int price;
    private UserProfileDto expertProfile;
    private JobAssignmentStatus jobAssignmentStatus;

    public JobAssignmentResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public UserProfileDto getExpertProfile() {
        return expertProfile;
    }

    public void setExpertProfile(UserProfileDto expertProfile) {
        this.expertProfile = expertProfile;
    }

    public JobAssignmentStatus getJobAssignmentStatus() {
        return jobAssignmentStatus;
    }

    public void setJobAssignmentStatus(JobAssignmentStatus jobAssignmentStatus) {
        this.jobAssignmentStatus = jobAssignmentStatus;
    }
}
