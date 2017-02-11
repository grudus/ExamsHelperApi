package com.grudus.examshelper.users.permissions;


import com.grudus.examshelper.users.User;

public class UserPermission {

            private Long id;

                private User user;

            private Permission permission;

    public UserPermission() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
