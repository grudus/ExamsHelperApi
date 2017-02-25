package com.grudus.examshelper.configuration.security;

import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.grudus.examshelper.users.UserState.ENABLED;
import static java.lang.String.format;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        Object credentials = authentication.getCredentials();

        if (username == null || credentials == null)
            throw new BadCredentialsException("Bad credentials");

        String password = (credentials.toString().trim());
        User user = userService.findByUsernameAndFetchRoles(username.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user: " + username));

        assertUserIsValid(user);

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new BadCredentialsException("Bad credentials");

        return new AuthenticatedUser(user);
    }

    private void assertUserIsValid(User user) {
        if (user.getState() != ENABLED)
            throw new DisabledException(format("User [%s] is disabled", user.getUsername()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
