package com.grudus.examshelper.users;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.grudus.examshelper.Utils.randAlph;
import static com.grudus.examshelper.Utils.randomUser;
import static com.grudus.examshelper.users.UserState.ENABLED;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;
    private User user;

    @Before
    public void init() {
        when(passwordEncoder.encode(anyString())).thenReturn("");
        userService = new UserService(userDao, passwordEncoder);
        user = randomUser();
    }

    @Test
    public void shouldFetchRolesWhenFoundByToken() {
        String token = randAlph(32);
        when(userDao.findByTokenWithState(anyString(), eq(ENABLED))).thenReturn(Optional.of(user));

        userService.findEnabledByToken(token);

        verify(userDao).findByTokenWithState(token, ENABLED);
        verify(userDao).fetchUserRoles(user);
    }

    @Test
    public void shouldFetchRolesWhenFoundByUsernameWithRoles() {
        String username = randAlph(10);
        when(userDao.findEnabledByUsername(anyString())).thenReturn(Optional.of(user));

        userService.findEnabledByUsername(username);

        verify(userDao).findEnabledByUsername(username);
        verify(userDao).fetchUserRoles(user);
    }
}
