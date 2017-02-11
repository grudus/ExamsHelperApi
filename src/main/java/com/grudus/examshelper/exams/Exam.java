package com.grudus.examshelper.exams;

import com.grudus.examshelper.subjects.Subject;

import java.time.LocalDateTime;
import java.util.Date;

public class Exam {

    private long id;
    private Long androidId;
    private String examInfo;
    private Date date;
    private Double grade;
    private LocalDateTime lastModified;
    private Subject subject;

    public Exam() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getAndroidId() {
        return androidId;
    }

    public void setAndroidId(Long androidId) {
        this.androidId = androidId;
    }

    public String getExamInfo() {
        return examInfo;
    }

    public void setExamInfo(String examInfo) {
        this.examInfo = examInfo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
