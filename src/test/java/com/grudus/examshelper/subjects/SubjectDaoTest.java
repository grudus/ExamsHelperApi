package com.grudus.examshelper.subjects;

import com.grudus.examshelper.SpringBasedTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.grudus.examshelper.Tables.SUBJECTS;
import static com.grudus.examshelper.Tables.USERS;
import static com.grudus.examshelper.Utils.randAlph;
import static com.grudus.examshelper.Utils.randomSubject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class SubjectDaoTest extends SpringBasedTest {

    private Subject subject1, subject2;
    private Long userId;

    @Autowired
    private SubjectDao dao;

    @Before
    public void init() {
        userId = dsl.insertInto(USERS, USERS.USERNAME).values(randAlph(15)).returning(USERS.ID).fetchOne().getId();
        subject1 = randomSubject(userId);
        subject2 = randomSubject(userId);

        dao.save(subject1);
        dao.save(subject2);
    }

    @Test
    public void shouldSaveSubject() {
        assertEquals(2, dsl.selectFrom(SUBJECTS).execute());
    }

    @Test
    public void shouldFindAllByUserId() {
        List<Subject> subjectList = dao.findByUserId(userId);

        assertEquals(2, subjectList.size());
    }

    @Test
    public void shouldFindById() {
        List<Subject> subjectList = dao.findByUserId(userId);

        Subject dbSubject = dao.findById(subjectList.get(0).getId()).get();


        assertEquals(subjectList.get(0).getLabel(), dbSubject.getLabel());
    }

    @Test
    public void shouldUpdateSubject() {
        // TODO: 01.03.17 find way to get id from save method
        List<Subject> subjectList = dao.findByUserId(userId);

        Subject subject = subjectList.get(0);
        String previousLabel = subject.getLabel();

        subject.setLabel(randAlph(44));
        dao.update(subject);

        Subject dbSubject = dao.findById(subject.getId()).get();

        assertNotEquals(previousLabel, dbSubject.getLabel());
    }

}
