package com.grudus.examshelper.controllers;

import com.grudus.examshelper.configuration.authenticated.AuthenticatedUser;
import com.grudus.examshelper.domain.User;
import com.grudus.examshelper.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAllUsers(AuthenticatedUser currentUser) {
        System.err.println("auth: " + currentUser);
        return userService.findAll();
    }

}
