package com.grudus.examshelper.exams;

import com.grudus.examshelper.SpringBasedTest;
import com.grudus.examshelper.subjects.Subject;
import com.grudus.examshelper.subjects.SubjectDao;
import com.grudus.examshelper.tables.records.SubjectsRecord;
import com.grudus.examshelper.users.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.grudus.examshelper.Tables.EXAMS;
import static com.grudus.examshelper.Tables.SUBJECTS;
import static com.grudus.examshelper.Utils.randomExam;
import static com.grudus.examshelper.Utils.randomSubject;
import static com.grudus.examshelper.users.roles.RoleName.USER;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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


    private Subject addSubject(Long userId) {
        Subject subject = randomSubject(userId);
        SubjectsRecord record = dsl.newRecord(SUBJECTS, subject);
        record.insert();
        subject.setId(record.getId());
        return subject;
    }

}
