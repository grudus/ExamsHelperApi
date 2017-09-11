package com.grudus.examshelper.exams;

import com.grudus.examshelper.SpringBasedTest;
import com.grudus.examshelper.exams.domain.Exam;
import com.grudus.examshelper.exams.domain.ExamDto;
import com.grudus.examshelper.subjects.Subject;
import com.grudus.examshelper.subjects.SubjectDao;
import com.grudus.examshelper.users.User;
import org.jooq.exception.DataAccessException;
import org.junit.Before;
import org.junit.Test;
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
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class ExamDaoTest extends SpringBasedTest {

    private Exam exam1;
    private Exam exam2;

    private Subject subject;
    private User user;

    @Autowired
    private ExamDao dao;

    @Autowired
    private SubjectDao subjectDao;

    @Before
    public void init() {
        user = addUserWithRoles(USER);
        User user2 = addUserWithRoles(USER);
        subject = addSubject(user.getId());
        Subject subject2 = addSubject(user2.getId());

        exam1 = randomExam(subject.getId());
        exam2 = randomExam(subject2.getId());

        dao.save(exam1);
        dao.save(exam2);
    }

    @Test
    public void shouldSaveExams() {
        assertEquals(2, dsl.fetchCount(EXAMS));
    }

    @Test
    public void shouldReturnNewlyCreatedId() {
        Long id = dao.save(randomExam(subject.getId()));

        assertNotNull(id);
    }

    @Test(expected = DataAccessException.class)
    public void shouldNotBeAbleToSaveWithoutSubjectId() {
        dao.save(randomExam(null));
    }

    @Test(expected = DataAccessException.class)
    public void shouldNotBeAbleToSaveWithoutValidSubjectId() {
        dao.save(randomExam(new Random().nextLong()));
    }

    @Test
    public void shouldFindAllAsExamDtoByUserId() {
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
    public void shouldReturnEmptyListWhenFindByNotExistingUser() {
        List<ExamDto> exams = dao.findAllAsExams(new Random().nextLong());

        assertTrue(exams.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListWhenUserDoNotHaveAnyExams() {
        Long id = addUserWithRoles().getId();
        List<ExamDto> exams = dao.findAllAsExams(id);

        assertTrue(exams.isEmpty());
    }

    @Test
    public void shouldFindAllFromDate() {
        LocalDateTime bound = LocalDateTime.now().plusYears(11);
        dao.save(randomExam(subject.getId(), bound.plusDays(33)));
        dao.save(randomExam(subject.getId(), bound.plusDays(22)));
        dao.save(randomExam(subject.getId(), bound.minusDays(1)));

        List<ExamDto> exams = dao.findAllFromDate(user.getId(), bound);

        assertEquals(2, exams.size());
    }

    @Test
    public void shouldReturnEmptyListWhenNoExamsAfterDate() {
        LocalDateTime bound = LocalDateTime.now().plusYears(11);
        dao.save(randomExam(subject.getId(), bound.minusDays(2)));
        dao.save(randomExam(subject.getId(), bound.minusDays(1)));

        List<ExamDto> exams = dao.findAllFromDate(user.getId(), bound);

        assertTrue(exams.isEmpty());
    }

    @Test
    public void shouldCountNotGradedExams() {
        dao.save(randomPastExam(subject.getId(), null));
        dao.save(randomPastExam(addSubject(user.getId()).getId(), -1D));
        dao.save(randomPastExam(subject.getId(), 0D));
        dao.save(randomPastExam(subject.getId(), 5D));
        dao.save(randomExam(subject.getId(), -12D));

        int notGradedCount = dao.countNotGradedFromPast(subject.getUserId());

        assertEquals(3, notGradedCount);
    }

    @Test
    public void shouldCountNoExamsWithoutGrade() {
        dao.save(randomExam(subject.getId(), -1D));
        dao.save(randomPastExam(subject.getId(), 5D));

        int notGradedCount = dao.countNotGradedFromPast(subject.getUserId());

        assertEquals(0, notGradedCount);
    }

    @Test
    public void shouldFindOnlyWithoutGradeForSubject() {
        dao.save(randomPastExam(subject.getId(), -1D));
        dao.save(randomPastExam(subject.getId(), null));
        dao.save(randomPastExam(subject.getId(), 3D));

        List<ExamDto> exams = dao.findWithoutGradeForSubject(subject.getId());

        assertEquals(2, exams.size());
    }

    @Test
    public void shouldFindWithoutGradeOnlyForGivenSubject() {
        dao.save(randomPastExam(subject.getId(), -1D));
        dao.save(randomPastExam(subject.getId(), null));
        dao.save(randomPastExam(addSubject(user.getId()).getId(), -1D));
        dao.save(randomPastExam(addSubject(user.getId()).getId(), -1D));
        dao.save(randomPastExam(subject.getId(), 31D));

        List<ExamDto> exams = dao.findWithoutGradeForSubject(subject.getId());

        assertEquals(2, exams.size());
    }

    @Test
    public void shouldReturnEmptyWithoutGradesWhenNoExamsInSubject() {
        List<ExamDto> exams = dao.findWithoutGradeForSubject(addSubject(user.getId()).getId());

        assertTrue(exams.isEmpty());
    }

    @Test
    public void shouldReturnEmptyWithoutGradesWhenSubjectHasOnlyWithGrades() {
        dao.save(randomPastExam(subject.getId(), 12D));
        List<ExamDto> exams = dao.findWithoutGradeForSubject(subject.getId());

        assertTrue(exams.isEmpty());
    }

    @Test
    public void shouldFindWithoutGradeOnlyFromPast() {
        dao.save(randomExam(subject.getId(), (Double)null));
        dao.save(randomExam(subject.getId(), (Double)null));
        dao.save(randomPastExam(subject.getId(), null));

        List<ExamDto> exams = dao.findWithoutGradeForSubject(subject.getId());

        assertEquals(1, exams.size());
        assertNotNull(exams.get(0).getSubject().getLabel());
        assertNotNull(exams.get(0).getSubject().getColor());
    }

    @Test
    public void shouldDetectUserOwnership() {
        Long newUserId = addUserWithRoles().getId();
        Exam exam1 = randomExam(subject.getId());
        Exam exam2 = randomExam(addSubject(newUserId).getId());

        Long exam1Id = dao.save(exam1);
        dao.save(randomExam(subject.getId()));
        Long exam2Id = dao.save(exam2);

        assertFalse(dao.belongsToUser(newUserId, exam1Id));
        assertTrue(dao.belongsToUser(newUserId, exam2Id));
    }

    @Test
    public void shouldUpdateGrade() {
        Long examId = dao.save(randomExam(subject.getId(), (Double)null));
        Double grade = 4.0;

        dao.updateGrade(examId, grade);

        ExamDto exam = dao.findAllAsExams(user.getId()).stream().filter(e -> e.getId().equals(examId)).findFirst().get();

        assertEquals(grade, exam.getGrade(), 0.01);
    }

    @Test
    public void shouldNotUpdateGradeWhenExamDoesNotExists() {
        Double crazyGrade = 21.37;
        dao.updateGrade(randomId(), crazyGrade);

        dao.findAllAsExams(user.getId())
                .forEach(exam -> assertNotEquals(crazyGrade, exam.getGrade(), 0.01));
    }

    private Subject addSubject(Long userId) {
        Subject subject = randomSubject(userId);
        Long id = subjectDao.save(subject);
        subject.setId(id);
        return subject;
    }
}
