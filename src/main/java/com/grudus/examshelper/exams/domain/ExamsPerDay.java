package com.grudus.examshelper.exams.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.grudus.examshelper.commons.JsonLocalDateDeserializer;
import com.grudus.examshelper.commons.JsonLocalDateSerializer;

import java.time.LocalDate;
import java.util.List;

public class ExamsPerDay {
    @JsonSerialize(using = JsonLocalDateSerializer.class)
    @JsonDeserialize(using = JsonLocalDateDeserializer.class)
    private final LocalDate date;
    private final List<ExamDto> exams;

    public ExamsPerDay(LocalDate date, List<ExamDto> exams) {
        this.date = date;
        this.exams = exams;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<ExamDto> getExams() {
        return exams;
    }
}
