package com.grudus.examshelper.configuration.security;


import com.grudus.examshelper.MockitoExtension;
import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.UserService;
import com.grudus.examshelper.users.UserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.grudus.examshelper.utils.Utils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAuthenticationProviderTest {

    private static final String USERNAME = randAlph(20);
    private static final String PASSWORD = randAlph(20);

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserAuthenticationProvider userAuthenticationProvider;
    private Authentication authentication;

    @BeforeEach
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

    @Test
    public void shouldThrowExceptionWhenUsernameIsNull() {
        assertThrows(BadCredentialsException.class, () ->
                userAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(null, randAlph(13)))
        );
    }

    @Test
    public void shouldThrowExceptionWhenPasswordIsNull() {
        assertThrows(BadCredentialsException.class, () ->
                userAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(randAlph(13), null)));
    }

    @Test
    public void shouldThrowExceptionWhenUserIsNotInDb() {
        when(userService.findByUsernameAndFetchRoles(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD)));
    }

    @Test
    public void shouldThrowExceptionWhenPasswordsDoNotMatches() {
        when(userService.findByUsernameAndFetchRoles(anyString())).thenReturn(Optional.of(randomUser()));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () ->
                userAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD)));
    }

    @Test
    public void shouldThrowExceptionWhenUserIsNotEnabled() {
        User user = randomUser();
        when(userService.findByUsernameAndFetchRoles(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        user.setState(UserState.WAITING);

        assertThrows(DisabledException.class, () ->
                userAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD)));
    }
}
