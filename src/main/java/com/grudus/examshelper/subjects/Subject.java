package com.grudus.examshelper.subjects;

import com.grudus.examshelper.exceptions.InvalidColorException;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

public class Subject {

    static final int MAX_LABEL_LENGTH = 64;
    private static final String COLOR_REGEX = "^#(?:[0-9a-fA-F]{3}){1,2}$";

    private Long id;
    private Long androidId;
    private Long userId;
    private String label;
    private String color;
    private LocalDateTime lastModified;

    public Subject() {}

    public Subject(Long userId, String label, String color) {
        assertColor(color);
        assertLabelLength(label);

        this.userId = userId;
        this.label = label;
        this.color = color;
        this.lastModified = now();
    }

    SubjectDto toDto() {
        return new SubjectDto(id, label, color);
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
        assertLabelLength(label);
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        assertColor(color);
        this.color = color;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    private void assertLabelLength(String label) {
        if (StringUtils.isBlank(label) || label.length() >= MAX_LABEL_LENGTH)
            throw new IllegalArgumentException("Label has invalid length: " + label);
    }

    public static void assertColor(String color) {
        if (color == null || invalidColor(color))
            throw new InvalidColorException("Color {%s} is invalid", color);
    }

    private static boolean invalidColor(String color) {
        return !color.matches(COLOR_REGEX);
    }
}
