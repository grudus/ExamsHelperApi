package com.grudus.examshelper.subjects;


public class SubjectDto {
    private Long userId;
    private Long id;
    private String label;
    private String color;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public SubjectDto(Long userId, Long id, String label, String color) {

        this.userId = userId;
        this.id = id;
        this.label = label;
        this.color = color;
    }

    public SubjectDto() {
    }

    public Subject toSubject() {
        Subject s = new Subject(userId, label, color);
        s.setId(id);
        return s;
    }

}
