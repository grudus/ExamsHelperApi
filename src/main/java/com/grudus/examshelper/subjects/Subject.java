package com.grudus.examshelper.subjects;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public class Subject {

    private Long id;
    private Long androidId;
    private Long userId;
    private String label;
    private String color;
    private LocalDateTime lastModified;

    public Subject() {}

    public Subject(Long userId, String label, String color) {
        this.userId = userId;
        this.label = label;
        this.color = color;
        this.lastModified = now();
    }

    public SubjectDto toDto() {
        return new SubjectDto(userId, id, label, color);
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }
}
