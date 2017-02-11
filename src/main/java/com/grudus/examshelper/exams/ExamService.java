package com.grudus.examshelper.exams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamService {

    private final ExamDao examDao;

    @Autowired
    public ExamService(ExamDao examDao) {
        this.examDao = examDao;
    }
}
