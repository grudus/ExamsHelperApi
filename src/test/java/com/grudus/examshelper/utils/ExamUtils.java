package com.grudus.examshelper.utils;

import com.grudus.examshelper.exams.Exam;

import java.time.LocalDateTime;

import static com.grudus.examshelper.utils.Utils.randAlph;
import static com.grudus.examshelper.utils.Utils.randomDate;
import static java.time.LocalDateTime.now;

public class ExamUtils {

    public static Exam randomExam(Long subjectId) {
        return randomExam(subjectId, randomDate(now(), now().plusYears(1)));
    }

    public static Exam randomExam(Long subjectId, LocalDateTime date) {
        return new Exam(randAlph(12), date, (Math.random() * 6), subjectId);
    }

}
