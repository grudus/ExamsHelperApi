package com.grudus.examshelper.users;

import com.grudus.examshelper.users.auth.AddUserRequest;
import com.grudus.examshelper.users.roles.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.grudus.examshelper.users.UserState.ENABLED;
import static com.grudus.examshelper.users.UserState.WAITING;
import static java.util.Collections.singletonList;

@Service
@Transactional
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public Optional<User> findByUsernameAndFetchRoles(String username) {
        Optional<User> user = userDao.findByUsername(username);
        user.ifPresent(userDao::fetchUserRoles);
        return user;
    }

    public Optional<User> findEnabledByToken(String token) {
        Optional<User> user = userDao.findByTokenWithState(token, ENABLED);
        user.ifPresent(userDao::fetchUserRoles);
        return user;
    }

    public void delete(User user) {
        userDao.delete(user.getId());
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public void update(User user) {
        userDao.update(user);
    }

    public void addToken(Long id, String token) {
        userDao.addToken(id, token);
    }

    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public Optional<User> findByEmailOrUsername(String email, String username) {
        return email != null ? findByEmail(email) : findByUsername(username);
    }

    public void saveAddUserRequest(AddUserRequest request, String token) {
        userDao.saveAddUserRequest(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getEmail(), token);
    }

    public Optional<User> findWaitingByToken(String token) {
        return userDao.findByTokenWithState(token, WAITING);
    }

    public void registerUser(User user, RoleName role) {
        userDao.updateState(user.getId(), ENABLED);
        userDao.addRoles(user, singletonList(role));
    }
}
