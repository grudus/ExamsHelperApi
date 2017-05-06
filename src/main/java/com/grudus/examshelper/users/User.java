package com.grudus.examshelper.users;

import com.grudus.examshelper.users.roles.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;

public class User {

    private Long id;
    private String email;
    private String password;
    private String username;
    private LocalDateTime registerDate;
    private LocalDateTime lastModified;
    private String token;
    private List<Role> roles;
    private UserState state;

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.state = UserState.ENABLED;
        this.registerDate = now();
        this.lastModified = now();
        this.roles = emptyList();
    }

    public UserDto toDto() {
        return new UserDto(id, email, username, registerDate);
    }

    public List<GrantedAuthority> generateAuthorities() {
        return this.roles.stream()
                .map(role -> "ROLE_" + role.getName().toString())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }
}