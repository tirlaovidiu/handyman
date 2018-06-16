package com.qubiz.server.util.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 15.06.2018 #
 ******************************
*/
public class OAuth2ClientAuthenticationProcessingAndSavingFilter extends OAuth2ClientAuthenticationProcessingFilter {
    public OAuth2ClientAuthenticationProcessingAndSavingFilter(String s) {
        super(s);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        System.out.println("Success login");
        //TODO check if user is present in db and do the job;

    }
}
