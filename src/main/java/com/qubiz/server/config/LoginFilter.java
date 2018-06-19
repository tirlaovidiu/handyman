package com.qubiz.server.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.qubiz.server.service.UserRegister;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 16.06.2018 #
 ******************************
*/

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private AuthorizationCodeResourceDetails resourceServerProperties;
    private static final JacksonFactory jacksonFactory = new JacksonFactory();
    private static final ApacheHttpTransport httpTransport = new ApacheHttpTransport();

    private UserRegister userRegister;

    protected LoginFilter(AuthorizationCodeResourceDetails resourceServerProperties, String defaultFilterProcessesUrl, UserRegister userRegister) {
        super(defaultFilterProcessesUrl);
        this.resourceServerProperties = resourceServerProperties;
        this.userRegister = userRegister;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

        String token = request.getParameter("token");

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jacksonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(resourceServerProperties.getClientId()))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(token);
        } catch (Exception e) {
            throw new BadCredentialsException("Could not obtain user details from token", e);
        }
        if (idToken != null) {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            GoogleIdToken.Payload payload = idToken.getPayload();

            UserDetails userDetails = new UserDetails();
            userDetails.setClientId(payload.getSubject());

            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_CLIENT");

            if ("expert".equals(request.getParameter("register"))) {
                userRegister.registerClient(payload, "expert");
                grantedAuthority = new SimpleGrantedAuthority("ROLE_EXPERT");
            } else {
                userRegister.registerClient(payload, "client");
            }

            authorities.add(grantedAuthority);
            response.setStatus(HttpServletResponse.SC_OK);

            this.setAuthenticationSuccessHandler(new SuccessAuthenticationHandler());

            return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        }
        throw new BadCredentialsException("Token error");
    }

}
