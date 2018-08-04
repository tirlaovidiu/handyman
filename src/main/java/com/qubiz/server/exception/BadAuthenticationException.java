package com.qubiz.server.exception;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 04.07.2018 #
 ******************************
*/

public class BadAuthenticationException extends RuntimeException {
    public BadAuthenticationException(String s) {
        super(s);
    }
}
