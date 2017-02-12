package com.grudus.examshelper.users;

import com.grudus.examshelper.users.auth.AddUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.grudus.examshelper.users.UserState.ENABLED;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userDao.findEnabledByUsername(username);
    }

    public Optional<User> findEnabledByUsername(String username) {
        Optional<User> user = userDao.findEnabledByUsername(username);
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

    public void saveAddUserRequest(AddUserRequest request, String token) {
        userDao.saveAddUserRequest(request.getUsername(), request.getPassword(), request.getEmail(), token);
    }
}
