package com.grudus.examshelper.users;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDao {

    Optional<User> findByUsername(String username) {return null;}

    Optional<User> findByToken(String token) {return null;}
}
