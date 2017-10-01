package com.grudus.examshelper.exams.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.grudus.examshelper.utils.Utils.randAlph;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExamDtoTest {

    @Test
    void shouldDetectIfHasGrade() throws Exception {
        ExamDto exam = new ExamDto(randAlph(11), LocalDateTime.now(), 4.0, null);

        assertTrue(exam.hasGrade());
    }

    @Test
    void shouldDetectNoGrade() throws Exception {
        ExamDto exam = new ExamDto(randAlph(11), LocalDateTime.now(), null, null);

        assertFalse(exam.hasGrade());
    }


    @Test
    void shouldDetectNoValidGrade() throws Exception {
        ExamDto exam = new ExamDto(randAlph(11), LocalDateTime.now(), 0.0, null);

        assertFalse(exam.hasGrade());
    }

}