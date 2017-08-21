package com.grudus.examshelper.subjects;

import com.grudus.examshelper.SpringBasedTest;
import org.jooq.exception.DataAccessException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.grudus.examshelper.Tables.SUBJECTS;
import static com.grudus.examshelper.Utils.*;
import static com.grudus.examshelper.utils.ListAssertionUtils.assertContainsProperties;
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

        Long id1 = dao.save(subject1);
        Long id2 = dao.save(subject2);
        subject1.setId(id1);
        subject2.setId(id2);
    }

    @Test
    public void shouldSaveSubject() {
        assertEquals(2, dsl.fetchCount(SUBJECTS));
    }

    @Test
    public void shouldReturnNewlyCreatedId() {
        Long id = dao.save(randomSubject(userId));

        assertNotNull(id);
        assertTrue(dao.findById(id).isPresent());
    }

    @Test(expected = DataAccessException.class)
    public void shouldNotBeAbleToSaveWhenUserDoNotExists() {
        dao.save(randomSubject(new Random().nextLong()));
    }

    @Test(expected = DataAccessException.class)
    public void shouldNotBeAbleToSaveWithoutLabel() {
        Subject subject = randomSubject(userId);
        subject.setLabel(null);

        dao.save(subject);
    }

    @Test(expected = DataAccessException.class)
    public void shouldNotBeAbleToSaveWhenLabelExistsForUser() {
        Subject subject = randomSubject(subject1.getUserId());
        subject.setLabel(subject1.getLabel());

        dao.save(subject);
    }

    @Test
    public void shouldBeAbleToSaveWhenLabelExistsForAnotherUser() {
        Long userId = addUserWithRoles().getId();
        Subject subject = randomSubject(userId);
        subject.setLabel(subject2.getLabel());

        dao.save(subject);
    }

    @Test
    public void shouldFindAllByUserId() {
        List<Subject> subjectList = dao.findByUserId(userId);

        assertEquals(2, subjectList.size());
        assertContainsProperties(subjectList, Subject::getLabel, subject1.getLabel(), subject2.getLabel());
    }

    @Test
    public void shouldReturnEmptyListWhenFindByNotExistingUserId() {
        assertTrue(dao.findByUserId(new Random().nextLong()).isEmpty());
    }

    @Test
    public void shouldFindById() {
        Subject dbSubject = dao.findById(subject2.getId()).get();

        assertEquals(subject2.getLabel(), dbSubject.getLabel());
        assertEquals(subject2.getColor(), dbSubject.getColor());
        assertEquals(subject2.getUserId(), dbSubject.getUserId());
    }

    @Test
    public void shouldNotFindById() {
        assertFalse(dao.findById(new Random().nextLong()).isPresent());
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
    public void shouldBeAbleToUpdateWithoutChanges() {
        dao.update(subject1);

        Subject dbSubject = dao.findById(subject1.getId()).get();

        assertEquals(subject1.getLabel(), dbSubject.getLabel());
        assertEquals(subject1.getColor(), dbSubject.getColor());
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

    @Test
    public void shouldFindByLabel() {
        Optional<SubjectDto> s = dao.findByUserIdAndLabel(userId, subject1.getLabel());

        assertTrue(s.isPresent());
        assertEquals(subject1.getLabel(), s.get().getLabel());
        assertEquals(subject1.getColor(), s.get().getColor());
    }

    @Test
    public void shouldReturnEmptyWhenFindNonExistingSubjectByLabel() {
        Optional<SubjectDto> s = dao.findByUserIdAndLabel(userId, randAlph(11));

        assertFalse(s.isPresent());
    }

    @Test
    public void shouldReturnEmptyWhenFindByAnotherUserAndLabel() {
        Long id2 = addUserWithRoles().getId();

        Optional<SubjectDto> s = dao.findByUserIdAndLabel(id2, subject1.getLabel());

        assertFalse(s.isPresent());
    }

    @Test
    public void shouldReturnEmptyWhenFindByNonExistingUserAndLabel() {
        Optional<SubjectDto> s = dao.findByUserIdAndLabel(new Random().nextLong(), subject1.getLabel());

        assertFalse(s.isPresent());
    }


}
