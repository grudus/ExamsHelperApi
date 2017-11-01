package com.grudus.examshelper.exams;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamsSecurityService {

    private final ExamService examService;

    @Autowired
    public ExamsSecurityService(ExamService examService) {
        this.examService = examService;
    }

    public boolean hasAccessToExam(AuthenticatedUser user, Long examId) {
        return !examService.belongsToAnotherUser(user.getUserId(), examId);
    }
}
