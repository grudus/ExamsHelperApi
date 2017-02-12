package com.grudus.examshelper.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return userDao.findByUsername(username);
    }

    public Optional<User> findByUsernameWithRoles(String username) {
        Optional<User> user = userDao.findByUsername(username);
        user.ifPresent(userDao::fetchUserRoles);
        return user;
    }

    public Optional<User> findByToken(String token) {
        Optional<User> user = userDao.findByToken(token);
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
}
