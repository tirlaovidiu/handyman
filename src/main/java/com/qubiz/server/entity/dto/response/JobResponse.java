package com.qubiz.server.entity.dto.response;

import com.qubiz.server.entity.JobStatus;
import com.qubiz.server.entity.dto.LocationDto;
import com.qubiz.server.entity.dto.UserProfileDto;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 04.07.2018 #
 ******************************
*/
public class JobResponse {
    private int jobId;
    private UserProfileDto clientProfile;
    private JobCategoryResponse jobCategory;
    private String jobDescription;
    private long arrivalDate;
    private LocationDto jobLocation;
    private JobStatus jobStatus;

    public JobResponse() {
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public UserProfileDto getClientProfile() {
        return clientProfile;
    }

    public void setClientProfile(UserProfileDto clientProfile) {
        this.clientProfile = clientProfile;
    }

    public JobCategoryResponse getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(JobCategoryResponse jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public long getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(long arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocationDto getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(LocationDto jobLocation) {
        this.jobLocation = jobLocation;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }
}
