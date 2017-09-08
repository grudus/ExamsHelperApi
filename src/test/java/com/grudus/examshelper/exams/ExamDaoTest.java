package com.grudus.examshelper.exams;

import com.grudus.examshelper.SpringBasedTest;
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
        addRoles();
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
        List<ExamDto> exams = dao.findAllAsExamDtoByUserId(user.getId());

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
        List<ExamDto> exams = dao.findAllAsExamDtoByUserId(new Random().nextLong());

        assertTrue(exams.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListWhenUserDoNotHaveAnyExams() {
        Long id = addUserWithRoles().getId();
        List<ExamDto> exams = dao.findAllAsExamDtoByUserId(id);

        assertTrue(exams.isEmpty());
    }

    @Test
    public void shouldFindAllFromDate() {
        LocalDateTime bound = LocalDateTime.now().plusYears(11);
        dao.save(randomExam(subject.getId(), bound.plusDays(33)));
        dao.save(randomExam(subject.getId(), bound.plusDays(22)));
        dao.save(randomExam(subject.getId(), bound.minusDays(1)));

        List<ExamDto> exams = dao.findAllByUserFromDate(user.getId(), bound);

        assertEquals(2, exams.size());
    }

    @Test
    public void shouldReturnEmptyListWhenNoExamsAfterDate() {
        LocalDateTime bound = LocalDateTime.now().plusYears(11);
        dao.save(randomExam(subject.getId(), bound.minusDays(2)));
        dao.save(randomExam(subject.getId(), bound.minusDays(1)));

        List<ExamDto> exams = dao.findAllByUserFromDate(user.getId(), bound);

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

    public void shouldCountNoExamsWithoutGrade() {
        dao.save(randomExam(subject.getId(), -1D));
        dao.save(randomPastExam(subject.getId(), 5D));

        int notGradedCount = dao.countNotGradedFromPast(subject.getUserId());

        assertEquals(0, notGradedCount);
    }


    private Subject addSubject(Long userId) {
        Subject subject = randomSubject(userId);
        Long id = subjectDao.save(subject);
        subject.setId(id);
        return subject;
    }

}
