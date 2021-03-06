package com.grudus.examshelper.users;

import com.grudus.examshelper.SpringBasedTest;
import com.grudus.examshelper.users.roles.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.grudus.examshelper.users.UserState.*;
import static com.grudus.examshelper.users.roles.RoleName.ADMIN;
import static com.grudus.examshelper.users.roles.RoleName.USER;
import static com.grudus.examshelper.utils.Utils.*;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest extends SpringBasedTest {

    private User user;

    @Autowired
    private UserDao userDao;

    @BeforeEach
    void init() {
        user = randomUser();
        userDao.save(user);
    }

    @Test
    void shouldSaveUser() {
        assertNotNull(user.getId());
        assertTrue(userDao.findByUsername(user.getUsername()).isPresent());
    }

    @Test
    void shouldAddWaitingUser() {
        String username = randAlph(10), password = randAlph(10), email = randomEmail(), token = randAlph(32);
        userDao.saveAddUserRequest(username, password, email, token);

        assertThat(userDao.findAll(), hasSize(2));
        assertThat(userDao.findAll(), hasItem(allOf(
                hasProperty("username", is(username)),
                hasProperty("password", is(password)),
                hasProperty("email", is(email)),
                hasProperty("state", is(WAITING))
                ))
        );
    }

    @Test
    void shouldFetchRoles() {
        userDao.addRoles(user, asList(USER, ADMIN));

        assertTrue(user.getRoles().isEmpty());

        userDao.fetchUserRoles(user);

        assertEquals(2, user.getRoles().size());
        assertThat(user.getRoles(), containsInAnyOrder(new Role(ADMIN), new Role(USER)));
    }

    @Test
    void shouldUpdateUser() {
        LocalDateTime before = now().minusSeconds(1);

        user.setUsername(randAlph(12));
        user.setPassword(randAlph(12));
        user.setEmail(randomEmail());
        user.setToken(randAlph(12));

        userDao.update(user);

        User updated = userDao.findById(user.getId()).get();

        assertEquals(user.getUsername(), updated.getUsername());
        assertEquals(user.getPassword(), updated.getPassword());
        assertEquals(user.getEmail(), updated.getEmail());
        assertEquals(user.getToken(), updated.getToken());
        assertEquals(user.getState(), updated.getState());
        assertTrue(updated.getLastModified().isAfter(before));
    }

    @Test
    void shouldNotBeAbleToSetLastModifiedToPast() {
        LocalDateTime now = now().minusSeconds(1);
        LocalDateTime past = now.minusDays(1);

        user.setLastModified(past);

        userDao.update(user);

        User updated = userDao.findById(user.getId()).get();

        assertFalse(updated.getLastModified().isEqual(past));
        assertTrue(updated.getLastModified().isAfter(now));
    }

    @Test
    void shouldAddRoles() {
        User dbUser = userDao.findById(user.getId()).get();
        userDao.fetchUserRoles(dbUser);
        assertThat(dbUser.getRoles(), anyOf(is(nullValue()), hasSize(0)));

        userDao.addRoles(user, asList(USER, ADMIN));

        dbUser = userDao.findById(user.getId()).get();
        userDao.fetchUserRoles(dbUser);
        assertEquals(2, dbUser.getRoles().size());
    }

    @Test
    void shouldDoNothingWhenAddRoleSecondTime() {
        userDao.addRoles(user, asList(USER, ADMIN));
        userDao.addRoles(user, asList(USER, ADMIN));
        userDao.addRoles(user, singletonList(USER));

        User dbUser = userDao.findById(user.getId()).get();
        userDao.fetchUserRoles(dbUser);
        assertEquals(2, dbUser.getRoles().size());
    }

    @Test
    void shouldAddToken() {
        String token = randAlph(32);
        LocalDateTime now = now().minusSeconds(1);

        assertThat(user.getToken(), isEmptyOrNullString());

        userDao.addToken(user.getId(), token);
        User updated = userDao.findById(user.getId()).get();

        assertEquals(token, updated.getToken());
        assertTrue(updated.getLastModified().isAfter(now));
    }

    @Test
    void shouldFindByUsernameWhenEnabled() {
        Optional<User> maybeUser = userDao.findByUsername(user.getUsername());

        assertTrue(maybeUser.isPresent());

        assertUsersEquality(user, maybeUser.get());
    }

    @Test
    void shouldFindByUsernameWhenWaiting() {
        String username = randAlph(10), password = randAlph(10), email = randomEmail(), token = randAlph(32);
        userDao.saveAddUserRequest(username, password, email, token);

        assertTrue(userDao.findByUsername(username).isPresent());
    }
    @Test
    void shouldNotFindByTokenWhenWaiting() {
        String username = randAlph(10), password = randAlph(10), email = randomEmail(), token = randAlph(32);
        userDao.saveAddUserRequest(username, password, email, token);

        assertFalse(userDao.findByTokenWithState(username, ENABLED).isPresent());
    }


    @Test
    void shouldFindById() {
        Optional<User> maybeUser = userDao.findById(user.getId());

        assertTrue(maybeUser.isPresent());

        assertUsersEquality(user, maybeUser.get());
    }

    @Test
    void shouldFindByToken() {
        String token = randAlph(32);
        userDao.addToken(user.getId(), token);

        Optional<User> maybeUser = userDao.findByTokenWithState(token, UserState.ENABLED);

        assertTrue(maybeUser.isPresent());

        assertUsersEquality(user, maybeUser.get());
    }

    @Test
    void shouldUpdateState() {
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        userDao.updateState(user.getId(), DISABLED);

        User newUser = userDao.findById(user.getId()).get();

        assertEquals(DISABLED, newUser.getState());
        assertTrue(newUser.getLastModified().isAfter(now));
    }

    @Test
    void shouldDeleteUser() {
        assertTrue(userDao.findById(user.getId()).isPresent());

        userDao.delete(user.getId());

        assertFalse(userDao.findById(user.getId()).isPresent());
    }

    @Test
    void shouldFindAll() {
        asList(randomUser(), randomUser(), randomUser())
                .forEach(userDao::save);

        assertEquals(4, userDao.findAll().size());
    }


    private void assertUsersEquality(User u1, User u2) {
        assertEquals(u1.getPassword(), u2.getPassword());
        assertEquals(u1.getEmail(), u2.getEmail());
        assertEquals(u1.getState(), u2.getState());
        assertEquals(u1.getUsername(), u2.getUsername());
    }
}
