package com.grudus.examshelper.configuration.authenticated;

import com.grudus.examshelper.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;



public class AuthenticatedUser extends UsernamePasswordAuthenticationToken {

    private final User user;

    AuthenticatedUser(User user) {
        super(user.getUsername(), user.getPassword(),  UserDetailsServiceImpl.generateAuthorities(user));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
