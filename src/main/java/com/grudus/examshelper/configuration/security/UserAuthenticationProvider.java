package com.grudus.examshelper.configuration.security;

import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    @Autowired
    public UserAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        Object credentials = authentication.getCredentials();

        if (username == null || credentials == null)
            throw new UsernameNotFoundException("Bad username or password");

        final String password = (credentials.toString().trim());
        final User user = userService.findEnabledByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user: " + username));

        if (!password.equals(user.getPassword()))
            throw new UsernameNotFoundException("Bad username or password");


        return new AuthenticatedUser(user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
