package com.grudus.examshelper.configuration.security;


import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.UserService;
import com.grudus.examshelper.users.UserState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.grudus.examshelper.utils.Utils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAuthenticationProviderTest {

    private static final String USERNAME = randAlph(20);
    private static final String PASSWORD = randAlph(20);

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserAuthenticationProvider userAuthenticationProvider;
    private Authentication authentication;

    @Before
    public void init() {
        userAuthenticationProvider = new UserAuthenticationProvider(userService, passwordEncoder);
        authentication = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
    }

    @Test
    public void shouldAuthenticateProperly() {
        String hash = randAlph(100);
        User user = new User(USERNAME, hash, randomEmail());
        when(userService.findByUsernameAndFetchRoles(USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASSWORD, hash)).thenReturn(true);

        AuthenticatedUser authUser = (AuthenticatedUser) userAuthenticationProvider.authenticate(authentication);

        assertEquals(user.getUsername(), authUser.getUser().getUsername());
        assertEquals(user.getPassword(), authUser.getUser().getPassword());
        assertEquals(user.getEmail(), authUser.getUser().getEmail());
    }

    @Test(expected = BadCredentialsException.class)
    public void shouldThrowExceptionWhenUsernameIsNull() {
        userAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(null, randAlph(13)));
    }

    @Test(expected = BadCredentialsException.class)
    public void shouldThrowExceptionWhenPasswordIsNull() {
        userAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(randAlph(13), null));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowExceptionWhenUserIsNotInDb() {
        when(userService.findByUsernameAndFetchRoles(anyString())).thenReturn(Optional.empty());

        userAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD));
    }

    @Test(expected = BadCredentialsException.class)
    public void shouldThrowExceptionWhenPasswordsDoNotMatches() {
        when(userService.findByUsernameAndFetchRoles(anyString())).thenReturn(Optional.of(randomUser()));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        userAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD));
    }

    @Test(expected = DisabledException.class)
    public void shouldThrowExceptionWhenUserIsNotEnabled() {
        User user = randomUser();
        when(userService.findByUsernameAndFetchRoles(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        user.setState(UserState.WAITING);

        userAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD));
    }
}
