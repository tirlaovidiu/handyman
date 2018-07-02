package com.qubiz.server.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 02.07.2018 #
 ******************************
*/
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
public class TokenIntegrityException extends org.springframework.security.core.AuthenticationException {
    public TokenIntegrityException(String msg) {
        super(msg);
    }

    public TokenIntegrityException(String msg, Throwable t) {
        super(msg, t);
    }
}
