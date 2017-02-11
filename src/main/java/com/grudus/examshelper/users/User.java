package com.grudus.examshelper.users;

import com.grudus.examshelper.subjects.Subject;
import com.grudus.examshelper.users.permissions.UserPermission;

import java.util.Date;
import java.util.List;

public class User {

    private Long id;

    private String email;

    private String password;

    private String username;

    private Date registerDate;

    private Date lastModified;

    private String token;

    private List<UserPermission> permissionList;

    private List<Subject> subjectList;

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
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

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<UserPermission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<UserPermission> permissionList) {
        this.permissionList = permissionList;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }
}