package com.grudus.examshelper.subjects;


public class SubjectDto {
    private Long id;
    private String label;
    private String color;

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

    public SubjectDto(Long id, String label, String color) {
        this.id = id;
        this.label = label;
        this.color = color;
    }

    public SubjectDto() {
    }

    public Subject toSubject(Long userId) {
        Subject s = new Subject(userId, label, color);
        s.setId(id);
        return s;
    }

}
