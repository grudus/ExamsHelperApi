package com.grudus.examshelper.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "subjects")
public class Subject {

    @Column
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "android_id")
    private Long androidId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String label;

    @Column
    private String color;

    @Column(name = "last_modified")
    private Date lastModified;

    @Column(name = "has_grade")
    private Boolean hasGrade;

    public Subject() {}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAndroidId() {
        return androidId;
    }

    public void setAndroidId(Long androidId) {
        this.androidId = androidId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Boolean getHasGrade() {
        return hasGrade;
    }

    public void setHasGrade(Boolean hasGrade) {
        this.hasGrade = hasGrade;
    }
}
