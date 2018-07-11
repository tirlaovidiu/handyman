package com.qubiz.server.entity.dto.request;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 11.07.2018 #
 ******************************
*/
public class AddJobAssignmentRequest {
    private int price;

    public AddJobAssignmentRequest() {
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
