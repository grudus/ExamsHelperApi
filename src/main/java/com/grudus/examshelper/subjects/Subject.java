package com.grudus.examshelper.subjects;

import com.grudus.examshelper.exams.Exam;
import com.grudus.examshelper.users.User;

import java.util.Date;
import java.util.List;

public class Subject {

    private Long id;

    private Long androidId;

    private User user;

    private String label;

    private String color;

    private Date lastModified;

    private Boolean hasGrade;

    private List<Exam> examList;

    public Subject() {
    }


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

    public List<Exam> getExamList() {
        return examList;
    }

    public void setExamList(List<Exam> examList) {
        this.examList = examList;
    }
}
