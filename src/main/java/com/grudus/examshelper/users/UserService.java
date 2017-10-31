package com.grudus.examshelper.users;

import com.grudus.examshelper.users.auth.AddUserRequest;
import com.grudus.examshelper.users.roles.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.grudus.examshelper.users.UserState.ENABLED;
import static com.grudus.examshelper.users.UserState.WAITING;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

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
        requireNonNull(id);
        return userDao.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        requireNonNull(username);
        return userDao.findByUsername(username);
    }

    public Optional<User> findByUsernameAndFetchRoles(String username) {
        requireNonNull(username);
        Optional<User> user = userDao.findByUsername(username);
        user.ifPresent(userDao::fetchUserRoles);
        return user;
    }

    public Optional<User> findEnabledByToken(String token) {
        requireNonNull(token);
        Optional<User> user = userDao.findByTokenWithState(token, ENABLED);
        user.ifPresent(userDao::fetchUserRoles);
        return user;
    }

    void delete(User user) {
        requireNonNull(user);
        userDao.delete(user.getId());
    }

    List<User> findAll() {
        return userDao.findAll();
    }

    void update(User user) {
        requireNonNull(user);
        userDao.update(user);
    }

    public void addToken(Long id, String token) {
        requireNonNull(id);
        requireNonNull(token);
        userDao.addToken(id, token);
    }

    public Optional<User> findByEmail(String email) {
        requireNonNull(email);
        return userDao.findByEmail(email);
    }

    Optional<User> findByEmailOrUsername(@Nullable String email, @Nullable String username) {
        if (email == null && username == null)
            throw new IllegalArgumentException("Both email and username are nulls");

        return email != null ? findByEmail(email) : findByUsername(username);
    }

    public void saveAddUserRequest(AddUserRequest request, String token) {
        requireNonNull(request, token);
        userDao.saveAddUserRequest(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getEmail(), token);
    }

    public Optional<User> findWaitingByToken(String token) {
        requireNonNull(token);
        return userDao.findByTokenWithState(token, WAITING);
    }

    public void registerUser(User user, RoleName role) {
        requireNonNull(user);
        requireNonNull(role);
        userDao.updateState(user.getId(), ENABLED);
        userDao.addRoles(user, singletonList(role));
    }
}
