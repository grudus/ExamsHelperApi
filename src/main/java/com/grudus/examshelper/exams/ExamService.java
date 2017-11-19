package com.grudus.examshelper.exams;

import com.grudus.examshelper.exams.domain.CreateExamRequest;
import com.grudus.examshelper.exams.domain.ExamDto;
import com.grudus.examshelper.exams.domain.ExamsPerDay;
import com.grudus.examshelper.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class ExamService {

    private final ExamDao examDao;

    @Autowired
    public ExamService(ExamDao examDao) {
        this.examDao = examDao;
    }

    public List<ExamDto> findAllExamsAsDtoByUser(User user) {
        requireNonNull(user);
        return examDao.findAllAsExams(user.getId());
    }

    List<ExamsPerDay> findAllExamsPerDay(User user) {
        requireNonNull(user);
        return findAllExamsPerDay(user, null);
    }

    List<ExamsPerDay> findAllExamsPerDay(User user, @Nullable LocalDateTime dateFrom) {
        requireNonNull(user);
        List<ExamDto> exams = dateFrom == null
                ? findAllExamsAsDtoByUser(user)
                : findAllExamsByUserFromDate(user, dateFrom);

        return exams.stream()
                .collect(groupingBy(exam -> exam.getDate().toLocalDate()))
                .entrySet().stream()
                .map(entry -> new ExamsPerDay(entry.getKey(), entry.getValue()))
                .sorted(comparing(ExamsPerDay::getDate).reversed())
                .collect(toList());
    }

    Long save(CreateExamRequest createExamRequest) {
        requireNonNull(createExamRequest);
        return examDao.save(createExamRequest.toExam());
    }

    Integer countNotGraded(User user) {
        requireNonNull(user);
        return examDao.countNotGradedFromPast(user.getId());
    }

    List<ExamDto> findWithoutGrade(Long userId, @Nullable Long subjectId) {
        return subjectId == null
                ? examDao.findWithoutGrade(userId)
                : examDao.findWithoutGradeForSubject(subjectId);
    }

    boolean belongsToAnotherUser(Long userId, Long examId) {
        requireNonNull(userId);
        requireNonNull(examId);
        return examDao.belongsToAnotherUser(userId, examId);
    }

    void updateGrade(Long examId, @Nullable Double grade) {
        requireNonNull(examId);
        examDao.updateGrade(examId, grade);
    }

    void delete(Long examId) {
        requireNonNull(examId);
        examDao.delete(examId);
    }

    private List<ExamDto> findAllExamsByUserFromDate(User user, LocalDateTime dateFrom) {
        requireNonNull(user);
        requireNonNull(dateFrom);
        return examDao.findAllFromDate(user.getId(), dateFrom);
    }
}
