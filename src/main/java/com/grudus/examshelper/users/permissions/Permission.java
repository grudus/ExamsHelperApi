package com.grudus.examshelper.users.permissions;

import java.util.List;

public class Permission {

    private Long id;

    private PermissionLabel name;

    private List<UserPermission> userPermissions;

    public Permission() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PermissionLabel getName() {
        return name;
    }

    public void setName(PermissionLabel name) {
        this.name = name;
    }

    public List<UserPermission> getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(List<UserPermission> userPermissions) {
        this.userPermissions = userPermissions;
    }
}
