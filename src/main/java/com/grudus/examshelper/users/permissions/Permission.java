package com.grudus.examshelper.users.permissions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "permissions")
public class Permission {

    @Column
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private PermissionLabel name;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<UserPermission> userPermissions;

    public Permission() {}

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
