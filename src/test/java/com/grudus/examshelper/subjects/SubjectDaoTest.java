package com.grudus.examshelper.subjects;

import com.grudus.examshelper.SpringBasedTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static com.grudus.examshelper.Tables.SUBJECTS;
import static com.grudus.examshelper.Utils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.*;


public class SubjectDaoTest extends SpringBasedTest {

    private Subject subject1, subject2;
    private Long userId;

    @Autowired
    private SubjectDao dao;

    @Before
    public void init() {
        userId = addUserWithRoles().getId();
        subject1 = randomSubject(userId);
        subject2 = randomSubject(userId);

        dao.save(subject1);
        dao.save(subject2);
    }

    @Test
    public void shouldSaveSubject() {
        assertEquals(2, dsl.fetchCount(SUBJECTS));
    }

    @Test
    public void shouldFindAllByUserId() {
        List<Subject> subjectList = dao.findByUserId(userId);

        assertEquals(2, subjectList.size());
        assertThat(subjectList, allOf(
                hasItem(hasProperty("label", is(subject1.getLabel()))),
                hasItem(hasProperty("label", is(subject2.getLabel())))));
    }

    @Test
    public void shouldFindById() {
        Subject dbSubject = dao.findById(subject2.getId()).get();


        assertEquals(subject2.getLabel(), dbSubject.getLabel());
        assertEquals(subject2.getColor(), dbSubject.getColor());
        assertEquals(subject2.getUserId(), dbSubject.getUserId());
    }

    @Test
    public void shouldUpdateSubject() {
        String previousLabel = subject2.getLabel();
        String previousColor = subject2.getColor();
        LocalDateTime previousLastModified = subject2.getLastModified().minusSeconds(1);

        subject2.setLabel(randAlph(33));
        subject2.setColor(randomColor());

        dao.update(subject2);
        Subject dbSubject = dao.findById(subject2.getId()).get();

        assertNotEquals(previousLabel, dbSubject.getLabel());
        assertNotEquals(previousColor, dbSubject.getColor());
        assertTrue(previousLastModified.isBefore(dbSubject.getLastModified()));
    }

    @Test
    public void shouldDeleteSubjectWhenValidId() {
        dao.delete(subject2.getId());

        assertEquals(1, dsl.fetchCount(SUBJECTS));
        assertTrue(dao.findById(subject1.getId()).isPresent());
    }


    @Test
    public void shouldDeleteNothingWhenIdNotInDb() {
        dao.delete(-13L);

        assertEquals(2, dsl.fetchCount(SUBJECTS));
        assertTrue(dao.findById(subject1.getId()).isPresent());
        assertTrue(dao.findById(subject2.getId()).isPresent());
    }


}
