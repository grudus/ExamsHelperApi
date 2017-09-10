package com.grudus.examshelper.exams.domain;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public class Exam {

    private long id;
    private Long androidId;
    private String info;
    private LocalDateTime date;
    private Double grade;
    private LocalDateTime lastModified;
    private Long subjectId;

    public Exam() {
    }

    public Exam(String info, LocalDateTime date, Double grade, Long subjectId) {
        this.info = info;
        this.date = date;
        this.grade = grade;
        this.subjectId = subjectId;
        this.lastModified = now();
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
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

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
}
