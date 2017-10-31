package com.grudus.examshelper.exams.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.grudus.examshelper.commons.json.JsonLocalDateTimeDeserializer;
import com.grudus.examshelper.commons.json.JsonLocalDateTimeSerializer;

import java.time.LocalDateTime;

public class CreateExamRequest {
    private final String info;
    private final Long subjectId;
    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private final LocalDateTime date;

    @JsonCreator
    public CreateExamRequest(
            @JsonProperty("info") String info,
            @JsonProperty("subjectId") Long subjectId,
            @JsonProperty("date") LocalDateTime date) {
        this.info = info;
        this.subjectId = subjectId;
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public LocalDateTime getDate() {
        return date;
    }
    
    public Exam toExam() {
        return new Exam(info, date, null, subjectId);
    }
}
