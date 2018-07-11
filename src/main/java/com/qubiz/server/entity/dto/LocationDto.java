package com.qubiz.server.entity.dto;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 06.07.2018 #
 ******************************
*/
public class LocationDto {
    private String addressName;
    private Double latitude;
    private Double longitude;

    public LocationDto() {
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
