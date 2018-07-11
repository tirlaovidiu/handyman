package com.qubiz.server.service;

import com.qubiz.server.entity.dto.UserProfileDto;
import com.qubiz.server.entity.model.User;
import com.qubiz.server.exception.BadAuthenticationException;
import com.qubiz.server.exception.HttpHandyManException;
import com.qubiz.server.repository.UserDao;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 02.07.2018 #
 ******************************
*/
@Service
public class UserService {

    private final UserDao userDao;
    private final MapperFacade mapperFacade;

    @Autowired
    public UserService(UserDao userDao, MapperFacade mapperFacade) {
        this.userDao = userDao;
        this.mapperFacade = mapperFacade;
    }

    public UserProfileDto getPersonalProfile(int clientId) {

        Optional<User> user = userDao.findById(clientId);
        if (user.isPresent()) {
            return mapperFacade.map(user.get(), UserProfileDto.class);
        }
        throw new BadAuthenticationException("No user match for your session");
    }

    public UserProfileDto getUser(int userId) {
        Optional<User> user = userDao.findById(userId);
        if (user.isPresent()) {
            return mapperFacade.map(user.get(), UserProfileDto.class);
        }
        throw new HttpHandyManException("No user found", HttpStatus.NOT_FOUND.value());
    }

    public void updateProfile(int clientId, UserProfileDto userProfileDto) {
        Optional<User> user = userDao.findById(clientId);
        if (!user.isPresent()) {
            throw new BadAuthenticationException("No user match for your session");
        }
        if (userProfileDto.getId() != user.get().getId()) {
            throw new HttpHandyManException("You don't have permission to modify this user", HttpStatus.FORBIDDEN.value());
        }

        //TODO validate userProfileDto
        User modifiedUser = user.get();
        modifiedUser.setUsername(userProfileDto.getUsername());
        modifiedUser.setFirstName(userProfileDto.getFirstName());
        modifiedUser.setLastName(userProfileDto.getLastName());
        modifiedUser.setProfilePictureUrl(userProfileDto.getProfilePictureUrl());
        modifiedUser.setPaymentType(userProfileDto.getPaymentType());

        if (modifiedUser.getExpertDescription() != null)
            modifiedUser.setExpertDescription(userProfileDto.getExpertDescription());

        userDao.save(modifiedUser);
    }
}
