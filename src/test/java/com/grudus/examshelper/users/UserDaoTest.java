package com.grudus.examshelper.users;

import com.grudus.examshelper.DaoTest;
import com.grudus.examshelper.users.roles.Role;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.grudus.examshelper.Utils.randAlph;
import static com.grudus.examshelper.Utils.randomEmail;
import static com.grudus.examshelper.users.roles.RoleName.ADMIN;
import static com.grudus.examshelper.users.roles.RoleName.USER;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.*;

public class UserDaoTest extends DaoTest {

    private User user;

    @Autowired
    private UserDao userDao;
    
    @Before
    public void init() {
        addRoles();
        user = new User(randAlph(10), randAlph(10), randomEmail());
        userDao.save(user);
    }
    

    @Test
    public void shouldSaveUser() {
        assertNotNull(user.getId());
        assertTrue(userDao.findByUsername(user.getUsername()).isPresent());
    }
    
    @Test
    public void shouldFetchRoles() {
        addRole(user.getUsername(), ADMIN);
        addRole(user.getUsername(), USER);
        
        assertTrue(user.getRoles().isEmpty());
        
        userDao.fetchUserPermissions(user);

        assertEquals(2, user.getRoles().size());
        assertThat(user.getRoles(), containsInAnyOrder(new Role(ADMIN), new Role(USER)));
    }
    
    @Test
    public void shouldUpdateUser() {
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
    public void shouldNotBeAbleToSetLastModifiedToPast() {
        LocalDateTime now = now().minusSeconds(1);
        LocalDateTime past = now.minusDays(1);

        user.setLastModified(past);

        userDao.update(user);

        User updated = userDao.findById(user.getId()).get();

        assertFalse(updated.getLastModified().isEqual(past));
        assertTrue(updated.getLastModified().isAfter(now));
    }

    @Test
    public void shouldAddToken() {
        String token = randAlph(32);

        assertThat(user.getToken(), isEmptyOrNullString());

        userDao.addToken(user.getId(), token);
        User updated = userDao.findById(user.getId()).get();

        assertEquals(token, updated.getToken());
    }
}
