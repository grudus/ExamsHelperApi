package com.grudus.examshelper.configuration.security;

import com.grudus.examshelper.users.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;



public class AuthenticatedUser extends UsernamePasswordAuthenticationToken {

    private final User user;

    public AuthenticatedUser(User user) {
        super(user.getUsername(), user.getPassword(),  user.generateAuthorities());
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
