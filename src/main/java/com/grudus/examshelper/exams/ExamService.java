package com.grudus.examshelper.exams;

import com.grudus.examshelper.exams.domain.CreateExamRequest;
import com.grudus.examshelper.exams.domain.ExamDto;
import com.grudus.examshelper.exams.domain.ExamsPerDay;
import com.grudus.examshelper.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;
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
        return examDao.findAllAsExams(user.getId());
    }

    List<ExamsPerDay> findAllExamsPerDay(User user) {
        return findAllExamsPerDay(user, null);
    }

    List<ExamsPerDay> findAllExamsPerDay(User user, LocalDateTime dateFrom) {
        List<ExamDto> exams = dateFrom == null
                ? findAllExamsAsDtoByUser(user)
                : findAllExamsByUserFromDate(user, dateFrom);

        return exams.stream()
                .collect(groupingBy(exam -> exam.getDate().toLocalDate()))
                .entrySet().stream()
                .map(entry -> new ExamsPerDay(entry.getKey(), entry.getValue()))
                .collect(toList());
    }

    Long save(CreateExamRequest createExamRequest) {
        return examDao.save(createExamRequest.toExam());
    }

    Integer countNotGraded(User user) {
        return examDao.countNotGradedFromPast(user.getId());
    }

    private List<ExamDto> findAllExamsByUserFromDate(User user, LocalDateTime dateFrom) {
        return examDao.findAllFromDate(user.getId(), dateFrom);
    }

    List<ExamDto> findWithoutGradeForSubject(Long subjectId) {
        return examDao.findWithoutGradeForSubject(subjectId);
    }
}
