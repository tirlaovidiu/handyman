package com.qubiz.server.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.qubiz.server.service.UserRegister;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 16.06.2018 #
 ******************************
*/
public class CustomFilter extends AbstractAuthenticationProcessingFilter {

    private AuthorizationCodeResourceDetails resourceServerProperties;
    private static final JacksonFactory jacksonFactory = new JacksonFactory();
    private static final ApacheHttpTransport httpTransport = new ApacheHttpTransport();

    @Autowired
    private UserRegister userRegister;


    protected CustomFilter(AuthorizationCodeResourceDetails resourceServerProperties, String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        this.resourceServerProperties = resourceServerProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String token = request.getParameter("token");

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jacksonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(resourceServerProperties.getClientId()))
                // Or, if multiple clients access the backend:
//                .setAudience(Arrays.asList(resourceServerProperties.getClientId(), "641526525870-3sni1poiuimvhht8o4ira0hq1jv0f52p.apps.googleusercontent.com"))
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
            userDetails.setEmail(payload.getEmail());

            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_CLIENT");

            if (request.getParameter("register").equals("client")) {
                userRegister.registerClient(payload);
            } else {
                userRegister.registerExpert(payload);
                grantedAuthority = new SimpleGrantedAuthority("ROLE_EXPERT");
            }

            authorities.add(grantedAuthority);
            response.setStatus(HttpServletResponse.SC_OK);
            return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        }
        throw new BadCredentialsException("Token error");
    }
}
