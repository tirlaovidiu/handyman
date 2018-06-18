package com.qubiz.server.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.qubiz.server.dao.RoleDao;
import com.qubiz.server.dao.UserDao;
import com.qubiz.server.entity.Role;
import com.qubiz.server.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 16.06.2018 #
 ******************************
*/
@Service
public class UserRegister {

    private final UserDao userDao;
    private RoleDao roleDao;

    @Autowired
    public UserRegister(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }


    public void registerClient(GoogleIdToken.Payload payload) {
        Optional<User> user = userDao.findUserByUserTokenId(payload.getSubject());
        User newClient = user.orElseGet(User::new);

        String name = (String) payload.get("name");
        String email = payload.getEmail();
        String pictureUrl = (String) payload.get("picture");
        String locale = (String) payload.get("locale");
        String familyName = (String) payload.get("family_name");
        String givenName = (String) payload.get("given_name");
        String userTokenId = payload.getSubject();

        //TODO make a validator

        newClient.setUsername(name);
        newClient.setFirstName(givenName);
        newClient.setLastName(familyName);
        newClient.setUserTokenId(userTokenId);

        Optional<Role> role = roleDao.findRoleByRoleName("client");
        Role newClientRole = role.orElseGet(Role::new);
        newClientRole.setRoleName("client");

        Set<Role> userRoles = newClient.getRoles();
        userRoles.add(newClientRole);

        newClient.setRoles(userRoles);

        userDao.save(newClient);
    }

    public void registerExpert(GoogleIdToken.Payload payload) {
        Optional<User> user = userDao.findUserByUserTokenId(payload.getSubject());
        User newExpert = user.orElseGet(User::new);

        String name = (String) payload.get("name");
        String email = payload.getEmail();
        String pictureUrl = (String) payload.get("picture");
        String locale = (String) payload.get("locale");
        String familyName = (String) payload.get("family_name");
        String givenName = (String) payload.get("given_name");
        String userTokenId = payload.getSubject();

        //TODO make a validator

        newExpert.setUsername(name);
        newExpert.setFirstName(givenName);
        newExpert.setLastName(familyName);
        newExpert.setUserTokenId(userTokenId);

        Optional<Role> role = roleDao.findRoleByRoleName("expert");
        Role newExpertRole = role.orElseGet(Role::new);
        newExpertRole.setRoleName("expert");

        Set<Role> userRoles = newExpert.getRoles();
        userRoles.add(newExpertRole);

        newExpert.setRoles(userRoles);

        userDao.save(newExpert);
    }
}
