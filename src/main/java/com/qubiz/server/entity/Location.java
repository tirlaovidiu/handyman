package com.qubiz.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 11.06.2018 #
 ******************************
*/
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String addressName;

    private Double latitude;
    private Double longitude;

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
