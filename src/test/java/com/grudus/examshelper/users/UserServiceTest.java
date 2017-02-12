package com.grudus.examshelper.users;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static com.grudus.examshelper.Utils.randAlph;
import static com.grudus.examshelper.Utils.randomUser;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    private UserService userService;

    private User user;

    @Before
    public void init() {
        userService = new UserService(userDao);
        user = randomUser();
    }

    @Test
    public void shouldFetchRolesWhenFoundByToken() {
        String token = randAlph(32);
        when(userDao.findByToken(anyString())).thenReturn(Optional.of(user));

        userService.findByToken(token);

        verify(userDao).findByToken(token);
        verify(userDao).fetchUserRoles(user);
    }

    @Test
    public void shouldFetchRolesWhenFoundByUsernameWithRoles() {
        String username = randAlph(10);
        when(userDao.findByUsername(anyString())).thenReturn(Optional.of(user));

        userService.findByUsernameWithRoles(username);

        verify(userDao).findByUsername(username);
        verify(userDao).fetchUserRoles(user);
    }
}
