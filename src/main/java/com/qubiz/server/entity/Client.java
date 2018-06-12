package com.qubiz.server.entity;

import javax.persistence.Entity;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 07.06.2018 #
 ******************************
*/
@Entity
public class Client extends User {
    private String paymentOption;

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }
}
