package com.grudus.examshelper.exams;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.grudus.examshelper.commons.JsonLocalDateTimeDeserializer;
import com.grudus.examshelper.commons.JsonLocalDateTimeSerializer;
import com.grudus.examshelper.subjects.SubjectDto;

import java.time.LocalDateTime;

public class ExamDto {
    private Long id;
    private String info;
    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDateTime date;
    private Double grade;
    private SubjectDto subject;


    public ExamDto() {
    }

    public ExamDto(String info, LocalDateTime date, Double grade, SubjectDto subject) {
        this.info = info;
        this.date = date;
        this.grade = grade;
        this.subject = subject;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public SubjectDto getSubject() {
        return subject;
    }

    public void setSubject(SubjectDto subject) {
        this.subject = subject;
    }
}
