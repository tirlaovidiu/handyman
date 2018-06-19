package com.qubiz.server.controller;

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

    @RequestMapping("/me")
    public String userMe() {
        return "Hi there !";
    }

}
