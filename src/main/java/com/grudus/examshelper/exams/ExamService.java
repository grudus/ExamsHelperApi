package com.grudus.examshelper.exams;

import com.grudus.examshelper.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    Map<LocalDate, List<ExamDto>> findAllExamsPerDay(User user) {
        return findAllExamsAsDtoByUser(user)
                .stream()
                .collect(Collectors.groupingBy(exam -> exam.getDate().toLocalDate()));
    }

    void save(CreateExamRequest createExamRequest) {
        examDao.save(createExamRequest.toExam());
    }
}
