package com.grudus.examshelper.exams;

import com.grudus.examshelper.SpringBasedTest;
import com.grudus.examshelper.exams.domain.Exam;
import com.grudus.examshelper.exams.domain.ExamDto;
import com.grudus.examshelper.subjects.Subject;
import com.grudus.examshelper.subjects.SubjectDao;
import com.grudus.examshelper.users.User;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static com.grudus.examshelper.Tables.EXAMS;
import static com.grudus.examshelper.users.roles.RoleName.USER;
import static com.grudus.examshelper.utils.ExamUtils.randomExam;
import static com.grudus.examshelper.utils.ExamUtils.randomPastExam;
import static com.grudus.examshelper.utils.Utils.randomId;
import static com.grudus.examshelper.utils.Utils.randomSubject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

class ExamDaoTest extends SpringBasedTest {

    private Exam exam1;
    private Exam exam2;

    private Subject subject;
    private User user;

    @Autowired
    private ExamDao dao;

    @Autowired
    private SubjectDao subjectDao;

    @BeforeEach
    void init() {
        user = addUserWithRoles(USER);
        User user2 = addUserWithRoles(USER);
        subject = addSubject(user.getId());
        Subject subject2 = addSubject(user2.getId());

        exam1 = randomExam(subject.getId());
        exam2 = randomExam(subject2.getId());

        exam1.setId(dao.save(exam1));
        exam2.setId(dao.save(exam2));
    }

    @Test
    void shouldSaveExams() {
        assertEquals(2, getExamsCount());
    }

    @Test
    void shouldReturnNewlyCreatedId() {
        Long id = dao.save(randomExam(subject.getId()));

        assertNotNull(id);
    }

    @Test
    void shouldNotBeAbleToSaveWithoutSubjectId() {
        assertThrows(DataAccessException.class, () ->
                dao.save(randomExam(null)));
    }

    @Test
    void shouldNotBeAbleToSaveWithoutValidSubjectId() {
        assertThrows(DataAccessException.class, () ->
                dao.save(randomExam(new Random().nextLong())));
    }

    @Test
    void shouldFindAllAsExamDtoByUserId() {
        List<ExamDto> exams = dao.findAllAsExams(user.getId());

        assertThat(exams, hasSize(1));
        ExamDto exam = exams.get(0);

        assertEquals(exam1.getGrade(), exam.getGrade());
        assertEquals(exam1.getDate(), exam.getDate());
        assertEquals(exam1.getInfo(), exam.getInfo());
        assertEquals(subject.getLabel(), exam.getSubject().getLabel());
        assertEquals(subject.getColor(), exam.getSubject().getColor());
    }

    @Test
    void shouldReturnEmptyListWhenFindByNotExistingUser() {
        List<ExamDto> exams = dao.findAllAsExams(new Random().nextLong());

        assertTrue(exams.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenUserDoNotHaveAnyExams() {
        Long id = addUserWithRoles().getId();
        List<ExamDto> exams = dao.findAllAsExams(id);

        assertTrue(exams.isEmpty());
    }

    @Test
    void shouldFindAllFromDate() {
        LocalDateTime bound = LocalDateTime.now().plusYears(11);
        dao.save(randomExam(subject.getId(), bound.plusDays(33)));
        dao.save(randomExam(subject.getId(), bound.plusDays(22)));
        dao.save(randomExam(subject.getId(), bound.minusDays(1)));

        List<ExamDto> exams = dao.findAllFromDate(user.getId(), bound);

        assertEquals(2, exams.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoExamsAfterDate() {
        LocalDateTime bound = LocalDateTime.now().plusYears(11);
        dao.save(randomExam(subject.getId(), bound.minusDays(2)));
        dao.save(randomExam(subject.getId(), bound.minusDays(1)));

        List<ExamDto> exams = dao.findAllFromDate(user.getId(), bound);

        assertTrue(exams.isEmpty());
    }

    @Test
    void shouldCountNotGradedExams() {
        dao.save(randomPastExam(subject.getId(), null));
        dao.save(randomPastExam(addSubject(user.getId()).getId(), -1D));
        dao.save(randomPastExam(subject.getId(), 0D));
        dao.save(randomPastExam(subject.getId(), 5D));
        dao.save(randomExam(subject.getId(), -12D));

        int notGradedCount = dao.countNotGradedFromPast(subject.getUserId());

        assertEquals(3, notGradedCount);
    }

    @Test
    void shouldCountNoExamsWithoutGrade() {
        dao.save(randomExam(subject.getId(), -1D));
        dao.save(randomPastExam(subject.getId(), 5D));

        int notGradedCount = dao.countNotGradedFromPast(subject.getUserId());

        assertEquals(0, notGradedCount);
    }

    @Test
    void shouldFindOnlyWithoutGradeForSubject() {
        dao.save(randomPastExam(subject.getId(), -1D));
        dao.save(randomPastExam(subject.getId(), null));
        dao.save(randomPastExam(subject.getId(), 3D));

        List<ExamDto> exams = dao.findWithoutGradeForSubject(subject.getId());

        assertEquals(2, exams.size());
    }

    @Test
    void shouldFindWithoutGradeOnlyForGivenSubject() {
        dao.save(randomPastExam(subject.getId(), -1D));
        dao.save(randomPastExam(subject.getId(), null));
        dao.save(randomPastExam(addSubject(user.getId()).getId(), -1D));
        dao.save(randomPastExam(addSubject(user.getId()).getId(), -1D));
        dao.save(randomPastExam(subject.getId(), 31D));

        List<ExamDto> exams = dao.findWithoutGradeForSubject(subject.getId());

        assertEquals(2, exams.size());
    }

    @Test
    void shouldReturnEmptyWithoutGradesWhenNoExamsInSubject() {
        List<ExamDto> exams = dao.findWithoutGradeForSubject(addSubject(user.getId()).getId());

        assertTrue(exams.isEmpty());
    }

    @Test
    void shouldReturnEmptyWithoutGradesWhenSubjectHasOnlyWithGrades() {
        dao.save(randomPastExam(subject.getId(), 12D));
        List<ExamDto> exams = dao.findWithoutGradeForSubject(subject.getId());

        assertTrue(exams.isEmpty());
    }

    @Test
    void shouldFindWithoutGradeOnlyFromPast() {
        dao.save(randomExam(subject.getId(), (Double) null));
        dao.save(randomExam(subject.getId(), (Double) null));
        dao.save(randomPastExam(subject.getId(), null));

        List<ExamDto> exams = dao.findWithoutGradeForSubject(subject.getId());

        assertEquals(1, exams.size());
        assertNotNull(exams.get(0).getSubject().getLabel());
        assertNotNull(exams.get(0).getSubject().getColor());
    }

    @Test
    void shouldFindWithoutGradesForAllSubjects() {
        dao.save(randomPastExam(subject.getId(), -1D));
        dao.save(randomPastExam(subject.getId(), null));
        dao.save(randomPastExam(addSubject(user.getId()).getId(), -1D));
        dao.save(randomPastExam(addSubject(user.getId()).getId(), -1D));
        dao.save(randomPastExam(subject.getId(), 31D));

        List<ExamDto> exams = dao.findWithoutGrade(user.getId());

        assertEquals(4, exams.size());

    }

    @Test
    void shouldDetectUserOwnership() {
        Long newUserId = addUserWithRoles().getId();
        Exam exam1 = randomExam(subject.getId());
        Exam exam2 = randomExam(addSubject(newUserId).getId());

        Long exam1Id = dao.save(exam1);
        dao.save(randomExam(subject.getId()));
        Long exam2Id = dao.save(exam2);

        assertTrue(dao.belongsToAnotherUser(newUserId, exam1Id));
        assertFalse(dao.belongsToAnotherUser(newUserId, exam2Id));
    }

    @Test
    void shouldUpdateGrade() {
        Long examId = dao.save(randomExam(subject.getId(), (Double) null));
        Double grade = 4.0;

        dao.updateGrade(examId, grade);

        ExamDto exam = dao.findAllAsExams(user.getId()).stream().filter(e -> e.getId().equals(examId)).findFirst().get();

        assertEquals(grade, exam.getGrade(), 0.01);
    }

    @Test
    void shouldNotUpdateGradeWhenExamDoesNotExists() {
        Double crazyGrade = 21.37;
        dao.updateGrade(randomId(), crazyGrade);

        dao.findAllAsExams(user.getId())
                .forEach(exam -> assertNotEquals(crazyGrade, exam.getGrade()));
    }

    @Test
    void shouldDeleteExam() {
        dao.delete(exam2.getId());

        assertEquals(1, getExamsCount());
    }

    @Test
    void shouldNotDeleteAnyExamWhenInvalidId() {
        dao.delete(randomId());

        assertEquals(2, getExamsCount());
    }

    private int getExamsCount() {
        return dsl.fetchCount(EXAMS);
    }

    private Subject addSubject(Long userId) {
        Subject subject = randomSubject(userId);
        Long id = subjectDao.save(subject);
        subject.setId(id);
        return subject;
    }
}
