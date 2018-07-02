package com.qubiz.server.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.qubiz.server.error.TokenIntegrityException;
import com.qubiz.server.service.AuthenticationUserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static com.qubiz.server.util.Constants.GOOGLE_API_KEY;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 16.06.2018 #
 ******************************
*/

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final JacksonFactory jacksonFactory = new JacksonFactory();
    private static final ApacheHttpTransport httpTransport = new ApacheHttpTransport();

    private AuthenticationUserService authenticationUserService;

    LoginFilter(String defaultFilterProcessesUrl, AuthenticationUserService authenticationUserService) {
        super(defaultFilterProcessesUrl);
        this.authenticationUserService = authenticationUserService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String token = request.getParameter("token");

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jacksonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singleton(GOOGLE_API_KEY))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(token);
            if (idToken == null) {
                throw new TokenIntegrityException("Google token error");
            }
        } catch (Exception e) {
            throw new TokenIntegrityException("Could not obtain user details from token", e);
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = authenticationUserService.authenticate(idToken);

        response.setStatus(HttpServletResponse.SC_OK);

        this.setAuthenticationSuccessHandler(new SuccessAuthenticationHandler());

        return usernamePasswordAuthenticationToken;
    }

}
