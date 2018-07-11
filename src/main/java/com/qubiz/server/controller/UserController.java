package com.qubiz.server.controller;

import com.qubiz.server.config.UserDetails;
import com.qubiz.server.entity.dto.UserProfileDto;
import com.qubiz.server.exception.BadAuthenticationException;
import com.qubiz.server.service.JobService;
import com.qubiz.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 18.06.2018 #
 ******************************
*/
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final JobService jobService;

    @Autowired
    public UserController(UserService userService, JobService jobService) {
        this.userService = userService;
        this.jobService = jobService;
    }

    @GetMapping("/me")
    public ResponseEntity getMyProfile() throws BadAuthenticationException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserProfileDto userProfile = userService.getPersonalProfile(userDetails.getClientId());

        return ResponseEntity.status(HttpStatus.OK).body(userProfile);
    }

    @PutMapping("/me")
    public ResponseEntity updateMyProfile(@RequestBody UserProfileDto userProfileDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        userService.updateProfile(userDetails.getClientId(), userProfileDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/jobs/categories")
    public ResponseEntity getJobCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserProfile(@PathVariable("id") int userId) {
        UserProfileDto user = userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
