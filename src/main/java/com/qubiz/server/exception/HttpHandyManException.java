package com.qubiz.server.exception;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 11.07.2018 #
 ******************************
*/
public class HttpHandyManException extends RuntimeException {
    private String message;
    private int code;

    public HttpHandyManException(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
