package com.grudus.examshelper.exams;

import com.grudus.examshelper.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExamService {

    private final ExamDao examDao;

    @Autowired
    public ExamService(ExamDao examDao) {
        this.examDao = examDao;
    }

    List<ExamDto> findAllExamsAsDtoByUser(User user) {
        return examDao.findAllAsExamDtoByUserId(user.getId());
    }
}
