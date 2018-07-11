package com.qubiz.server.entity.dto.request;

import com.qubiz.server.entity.dto.LocationDto;

import java.util.Date;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 05.07.2018 #
 ******************************
*/
public class AddJobRequest {
    private int jobCategoryId;
    private String description;
    private Date arrivalDate;
    private LocationDto location;

    public AddJobRequest() {
    }

    public AddJobRequest(int jobCategoryId, String description, Date arrivalDate, LocationDto location) {
        this.jobCategoryId = jobCategoryId;
        this.description = description;
        this.arrivalDate = arrivalDate;
        this.location = location;
    }

    public int getJobCategoryId() {
        return jobCategoryId;
    }

    public void setJobCategoryId(int jobCategoryId) {
        this.jobCategoryId = jobCategoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }
}
