package com.grudus.examshelper.users;

import com.grudus.examshelper.users.auth.AddUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.grudus.examshelper.Utils.*;
import static com.grudus.examshelper.users.UserState.ENABLED;
import static com.grudus.examshelper.users.roles.RoleName.ADMIN;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
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
        user = randomUser();
        userService = new UserService(userDao, passwordEncoder);

        when(passwordEncoder.encode(anyString())).thenReturn("");
        when(userDao.findById(anyLong())).thenReturn(Optional.of(user));
        when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userDao.findEnabledByUsername(anyString())).thenReturn(Optional.of(user));
        when(userDao.findByTokenWithState(anyString(), any(UserState.class))).thenReturn(Optional.of(user));
    }

    @Test
    public void shouldFindById() {
        User u = userService.findById(1L).get();

        verify(userDao).findById(eq(1L));
        assertEquals(user.getUsername(), u.getUsername());
    }

    @Test
    public void shouldFindEnabledByUsername() {
        User u = userService.findEnabledByUsername(user.getUsername()).get();

        verify(userDao).findEnabledByUsername(eq(user.getUsername()));
        assertEquals(user.getEmail(), u.getEmail());
    }

    @Test
    public void shouldFetchRolesWhenFoundByToken() {
        String token = randAlph(32);
        userService.findEnabledByToken(token);

        verify(userDao).findByTokenWithState(token, ENABLED);
        verify(userDao).fetchUserRoles(user);
    }

    @Test
    public void shouldFetchRolesWhenFoundByUsernameWithRoles() {
        String username = randAlph(10);

        userService.findEnabledByUsernameAndFetchRoles(username);

        verify(userDao).findEnabledByUsername(username);
        verify(userDao).fetchUserRoles(user);
    }

    @Test
    public void shouldDeleteUser() {
        userService.delete(user);

        verify(userDao).delete(eq(user.getId()));
    }

    @Test
    public void shouldFindAll() {
        when(userDao.findAll()).thenReturn(singletonList(user));

        List<User> all = userService.findAll();

        verify(userDao).findAll();
        assertEquals(all, userDao.findAll());
    }

    @Test
    public void shouldUpdate() {
        userService.update(user);

        verify(userDao).update(eq(user));
    }

    @Test
    public void shouldAddToken() {
        String token = randAlph(32);

        userService.addToken(user.getId(), token);

        verify(userDao).addToken(eq(user.getId()), eq(token));
    }

    @Test
    public void shouldFindByEmail() {
        User u = userService.findByEmail(user.getEmail()).get();

        verify(userDao).findByEmail(eq(user.getEmail()));
        assertEquals(user.getUsername(), u.getUsername());
    }

    @Test
    public void shouldEncodePasswordWhenSaveNewUser() {
        String hash = randAlph(100);
        String token = randAlph(32);
        AddUserRequest request = new AddUserRequest(randAlph(10), randAlph(10), randomEmail());

        when(passwordEncoder.encode(anyString())).thenReturn(hash);

        userService.saveAddUserRequest(request, token);

        verify(userDao).saveAddUserRequest(request.getUsername(), hash, request.getEmail(), token);
    }

    @Test
    public void shouldEnableUserWithRoleWhenRegisterUser() {
        userService.registerUser(user, ADMIN);

        verify(userDao).updateState(eq(user.getId()), eq(ENABLED));
        verify(userDao).addRoles(eq(user), eq(singletonList(ADMIN)));
    }

}
