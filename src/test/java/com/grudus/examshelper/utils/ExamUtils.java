package com.grudus.examshelper.utils;

import com.grudus.examshelper.exams.domain.Exam;

import java.time.LocalDateTime;

import static com.grudus.examshelper.utils.Utils.randAlph;
import static com.grudus.examshelper.utils.Utils.randomDate;
import static java.time.LocalDateTime.now;

public class ExamUtils {

    public static Exam randomExam(Long subjectId) {
        return randomExam(subjectId, randomExamDate());
    }

    public static Exam randomExam(Long subjectId, LocalDateTime date) {
        return randomExam(randAlph(12), date, (Math.random() * 6), subjectId);
    }

    public static Exam randomExam(Long subjectId, Double grade) {
        return randomExam(randAlph(12), randomExamDate(), grade, subjectId);
    }

    public static Exam randomPastExam(Long subjectId, Double grade) {
        return randomExam(randAlph(12), randomExamDate().minusYears(2), grade, subjectId);
    }

    public static Exam randomExam(String info, LocalDateTime date, Double grade, Long subjectId) {
        return new Exam(info, date, grade, subjectId);
    }


    private static LocalDateTime randomExamDate() {
        return randomDate(now(), now().plusYears(1));
    }

}
