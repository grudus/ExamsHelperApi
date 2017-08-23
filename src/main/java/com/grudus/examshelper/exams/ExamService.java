package com.grudus.examshelper.exams;

import com.grudus.examshelper.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
class ExamService {

    private final ExamDao examDao;

    @Autowired
    ExamService(ExamDao examDao) {
        this.examDao = examDao;
    }

    List<ExamDto> findAllExamsAsDtoByUser(User user) {
        return examDao.findAllAsExamDtoByUserId(user.getId());
    }

    List<ExamsPerDay> findAllExamsPerDay(User user) {
        return findAllExamsAsDtoByUser(user)
                .stream()
                .collect(Collectors.groupingBy(exam -> exam.getDate().toLocalDate()))
                .entrySet().stream()
                .map(entry -> new ExamsPerDay(entry.getKey(), entry.getValue()))
                .collect(toList());
    }

    Long save(CreateExamRequest createExamRequest) {
        return examDao.save(createExamRequest.toExam());
    }
}
