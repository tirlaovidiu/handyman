package com.qubiz.server.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.qubiz.server.config.UserDetails;
import com.qubiz.server.dao.RoleDao;
import com.qubiz.server.dao.UserDao;
import com.qubiz.server.entity.Role;
import com.qubiz.server.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 16.06.2018 #
 ******************************
*/
@Service
public class AuthenticationUserService {

    private static final String CLIENT_ROLE_NAME = "CLIENT";
    private final UserDao userDao;
    private RoleDao roleDao;

    private static Logger logger = Logger.getLogger("AuthenticateUser");

    @Autowired
    public AuthenticationUserService(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Transactional
    public UsernamePasswordAuthenticationToken authenticate(GoogleIdToken idToken) {
        GoogleIdToken.Payload payload = idToken.getPayload();
        Optional<User> dbUser = userDao.findUserByUserTokenId(payload.getSubject());

        Collection<GrantedAuthority> authorities = new HashSet<>();
        UserDetails userDetails = new UserDetails();

        if (dbUser.isPresent()) {
            Set<Role> roles = dbUser.get().getRoles();
            for (Role role : roles) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
                authorities.add(authority);
            }
            userDetails.setClientId(dbUser.get().getId());
        } else {
            User user = new User();

            String email = payload.getEmail();
            String locale = (String) payload.get("locale");
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");
            String userTokenId = payload.getSubject();

            user.setUsername(name);
            user.setFirstName(givenName);
            user.setLastName(familyName);
            user.setUserTokenId(userTokenId);

            URI uri = null;
            try {
                uri = new URI(pictureUrl);
            } catch (URISyntaxException e) {
                logger.log(Level.INFO, e.getMessage());
            }

            user.setProfilePictureUrl(uri);

            Optional<Role> dbRole = roleDao.findRoleByRoleName(CLIENT_ROLE_NAME);
            GrantedAuthority authority;
            if (dbRole.isPresent()) {
                authority = new SimpleGrantedAuthority(dbRole.get().getRoleName());
                user.setRoles(Collections.singleton(dbRole.get()));
            } else {
                Role role = new Role();
                role.setRoleName(CLIENT_ROLE_NAME);
                role = roleDao.save(role);
                authority = new SimpleGrantedAuthority(role.getRoleName());
                user.setRoles(Collections.singleton(role));
            }
            authorities.add(authority);
            userDetails.setClientId(userDao.save(user).getId());
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }
}
