package com.qubiz.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 03.07.2018 #
 ******************************
*/
@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BadAuthenticationException.class)
    protected void badAuthentication(RuntimeException e, HttpServletResponse response, HttpServletRequest request) throws IOException {
//        Log out user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    @ExceptionHandler(value = HttpHandyManException.class)
    protected void exceptionHandler(HttpHandyManException e, HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.sendError(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    protected void defaultHandler(RuntimeException e, HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong. Please login again !");
    }

}
