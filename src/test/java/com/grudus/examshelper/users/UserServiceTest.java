package com.grudus.examshelper.users;

import com.grudus.examshelper.MockitoExtension;
import com.grudus.examshelper.users.auth.AddUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.grudus.examshelper.users.UserState.ENABLED;
import static com.grudus.examshelper.users.roles.RoleName.ADMIN;
import static com.grudus.examshelper.utils.Utils.*;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;
    private User user;

    @BeforeEach
    void init() {
        user = randomUser();
        userService = new UserService(userDao, passwordEncoder);

        when(passwordEncoder.encode(anyString())).thenReturn("");
        when(userDao.findById(anyLong())).thenReturn(Optional.of(user));
        when(userDao.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userDao.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(userDao.findByTokenWithState(anyString(), any(UserState.class))).thenReturn(Optional.of(user));
    }

    @Test
    void shouldFindById() {
        User u = userService.findById(1L).get();

        verify(userDao).findById(eq(1L));
        assertEquals(user.getUsername(), u.getUsername());
    }

    @Test
    void shouldFindEnabledByUsername() {
        User u = userService.findByUsername(user.getUsername()).get();

        verify(userDao).findByUsername(eq(user.getUsername()));
        assertEquals(user.getEmail(), u.getEmail());
    }

    @Test
    void shouldFetchRolesWhenFoundByToken() {
        String token = randAlph(32);
        userService.findEnabledByToken(token);

        verify(userDao).findByTokenWithState(token, ENABLED);
        verify(userDao).fetchUserRoles(user);
    }

    @Test
    void shouldFetchRolesWhenFoundByUsernameWithRoles() {
        String username = randAlph(10);

        userService.findByUsernameAndFetchRoles(username);

        verify(userDao).findByUsername(username);
        verify(userDao).fetchUserRoles(user);
    }

    @Test
    void shouldDeleteUser() {
        userService.delete(user);

        verify(userDao).delete(eq(user.getId()));
    }

    @Test
    void shouldFindAll() {
        when(userDao.findAll()).thenReturn(singletonList(user));

        List<User> all = userService.findAll();

        verify(userDao).findAll();
        assertEquals(all, userDao.findAll());
    }

    @Test
    void shouldUpdate() {
        userService.update(user);

        verify(userDao).update(eq(user));
    }

    @Test
    void shouldAddToken() {
        String token = randAlph(32);

        userService.addToken(user.getId(), token);

        verify(userDao).addToken(eq(user.getId()), eq(token));
    }

    @Test
    void shouldFindByEmail() {
        User u = userService.findByEmail(user.getEmail()).get();

        verify(userDao).findByEmail(eq(user.getEmail()));
        assertEquals(user.getUsername(), u.getUsername());
    }

    @Test
    void shouldEncodePasswordWhenSaveNewUser() {
        String hash = randAlph(100);
        String token = randAlph(32);
        AddUserRequest request = new AddUserRequest(randAlph(10), randAlph(10), randomEmail());

        when(passwordEncoder.encode(anyString())).thenReturn(hash);

        userService.saveAddUserRequest(request, token);

        verify(userDao).saveAddUserRequest(request.getUsername(), hash, request.getEmail(), token);
    }

    @Test
    void shouldEnableUserWithRoleWhenRegisterUser() {
        userService.registerUser(user, ADMIN);

        verify(userDao).updateState(eq(user.getId()), eq(ENABLED));
        verify(userDao).addRoles(eq(user), eq(singletonList(ADMIN)));
    }

}
