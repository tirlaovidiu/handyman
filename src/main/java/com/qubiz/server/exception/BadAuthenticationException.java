package com.qubiz.server.exception;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 04.07.2018 #
 ******************************
*/

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BadAuthenticationException extends RuntimeException {
    public BadAuthenticationException(String s) {
        super(s);
    }
}
