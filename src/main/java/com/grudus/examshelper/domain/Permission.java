package com.grudus.examshelper.domain;

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
    private com.grudus.examshelper.domain.enums.Permission name;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserPermission> userPermissions;

    public Permission() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public com.grudus.examshelper.domain.enums.Permission getName() {
        return name;
    }

    public void setName(com.grudus.examshelper.domain.enums.Permission name) {
        this.name = name;
    }

    public List<UserPermission> getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(List<UserPermission> userPermissions) {
        this.userPermissions = userPermissions;
    }
}
