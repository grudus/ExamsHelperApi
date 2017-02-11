package com.grudus.examshelper.users.permissions;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grudus.examshelper.users.User;

import javax.persistence.*;

@Entity
@Table(name = "user_permissions")
public class UserPermission {

    @Id
    @JsonIgnore
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @JsonIgnore
    private User user;

    @JoinColumn(name = "permission_id")
    @ManyToOne
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
