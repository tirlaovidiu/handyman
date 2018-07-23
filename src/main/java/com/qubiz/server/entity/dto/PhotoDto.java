package com.qubiz.server.entity.dto;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 22.07.2018 #
 ******************************
*/
public class PhotoDto {
    private int id;
    private String awsUrl;

    public PhotoDto() {
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
}
