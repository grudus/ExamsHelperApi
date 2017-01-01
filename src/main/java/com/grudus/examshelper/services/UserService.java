package com.grudus.examshelper.services;

import com.grudus.examshelper.dao.UserDao;
import com.grudus.examshelper.domain.User;
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
        return Optional.ofNullable(userDao.findOne(id));
    }

    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public Optional<User> findByToken(String token) {return userDao.findByToken(token);}

    public void delete(User user) {
        userDao.delete(user.getId());
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public void update(User user) {
        userDao.save(user);
    }
}
