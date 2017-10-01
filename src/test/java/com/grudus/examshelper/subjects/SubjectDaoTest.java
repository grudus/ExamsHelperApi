package com.grudus.examshelper.subjects;

import com.grudus.examshelper.SpringBasedTest;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.grudus.examshelper.Tables.SUBJECTS;
import static com.grudus.examshelper.utils.ListAssertionUtils.assertContainsProperties;
import static com.grudus.examshelper.utils.Utils.*;
import static org.junit.jupiter.api.Assertions.*;


class SubjectDaoTest extends SpringBasedTest {

    private Subject subject1, subject2;
    private Long userId;

    @Autowired
    private SubjectDao dao;

    @BeforeEach
    void init() {
        userId = addUserWithRoles().getId();
        subject1 = randomSubject(userId);
        subject2 = randomSubject(userId);

        Long id1 = dao.save(subject1);
        Long id2 = dao.save(subject2);
        subject1.setId(id1);
        subject2.setId(id2);
    }

    @Test
    void shouldSaveSubject() {
        assertEquals(2, dsl.fetchCount(SUBJECTS));
    }

    @Test
    void shouldReturnNewlyCreatedId() {
        Long id = dao.save(randomSubject(userId));

        assertNotNull(id);
        assertTrue(dao.findById(id).isPresent());
    }

    @Test
    void shouldNotBeAbleToSaveWhenUserDoNotExists() {
        assertThrows(DataAccessException.class, () ->
                dao.save(randomSubject(new Random().nextLong())));
    }


    @Test
    void shouldNotBeAbleToSaveWhenLabelExistsForUser() {
        Subject subject = randomSubject(subject1.getUserId());
        subject.setLabel(subject1.getLabel());

        assertThrows(DataAccessException.class, () -> dao.save(subject));
    }

    @Test
    void shouldBeAbleToSaveWhenLabelExistsForAnotherUser() {
        Long userId = addUserWithRoles().getId();
        Subject subject = randomSubject(userId);
        subject.setLabel(subject2.getLabel());

        dao.save(subject);
    }

    @Test
    void shouldDetectExistence() {
        Long id = dao.save(randomSubject(userId));

        Boolean exists = dao.exists(id);

        assertTrue(exists);
    }

    @Test
    void shouldDetectNotExists() {
        Boolean exists = dao.exists(new Random().nextLong());

        assertFalse(exists);
    }

    @Test
    void shouldFindAllByUserId() {
        List<Subject> subjectList = dao.findByUserId(userId);

        assertEquals(2, subjectList.size());
        assertContainsProperties(subjectList, Subject::getLabel, subject1.getLabel(), subject2.getLabel());
    }

    @Test
    void shouldReturnEmptyListWhenFindByNotExistingUserId() {
        assertTrue(dao.findByUserId(new Random().nextLong()).isEmpty());
    }

    @Test
    void shouldFindById() {
        Subject dbSubject = dao.findById(subject2.getId()).get();

        assertEquals(subject2.getLabel(), dbSubject.getLabel());
        assertEquals(subject2.getColor(), dbSubject.getColor());
        assertEquals(subject2.getUserId(), dbSubject.getUserId());
    }

    @Test
    void shouldNotFindById() {
        assertFalse(dao.findById(new Random().nextLong()).isPresent());
    }

    @Test
    void shouldUpdateSubject() {
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
    void shouldBeAbleToUpdateWithoutChanges() {
        dao.update(subject1);

        Subject dbSubject = dao.findById(subject1.getId()).get();

        assertEquals(subject1.getLabel(), dbSubject.getLabel());
        assertEquals(subject1.getColor(), dbSubject.getColor());
    }

    @Test
    void shouldDeleteSubjectWhenValidId() {
        dao.delete(subject2.getId());

        assertEquals(1, dsl.fetchCount(SUBJECTS));
        assertTrue(dao.findById(subject1.getId()).isPresent());
    }


    @Test
    void shouldDeleteNothingWhenIdNotInDb() {
        dao.delete(-13L);

        assertEquals(2, dsl.fetchCount(SUBJECTS));
        assertTrue(dao.findById(subject1.getId()).isPresent());
        assertTrue(dao.findById(subject2.getId()).isPresent());
    }

    @Test
    void shouldFindByLabel() {
        Optional<SubjectDto> s = dao.findByUserIdAndLabel(userId, subject1.getLabel());

        assertTrue(s.isPresent());
        assertEquals(subject1.getLabel(), s.get().getLabel());
        assertEquals(subject1.getColor(), s.get().getColor());
    }

    @Test
    void shouldReturnEmptyWhenFindNonExistingSubjectByLabel() {
        Optional<SubjectDto> s = dao.findByUserIdAndLabel(userId, randAlph(11));

        assertFalse(s.isPresent());
    }

    @Test
    void shouldReturnEmptyWhenFindByAnotherUserAndLabel() {
        Long id2 = addUserWithRoles().getId();

        Optional<SubjectDto> s = dao.findByUserIdAndLabel(id2, subject1.getLabel());

        assertFalse(s.isPresent());
    }

    @Test
    void shouldReturnEmptyWhenFindByNonExistingUserAndLabel() {
        Optional<SubjectDto> s = dao.findByUserIdAndLabel(new Random().nextLong(), subject1.getLabel());

        assertFalse(s.isPresent());
    }

    @Test
    void shouldDetectLabelExistence() {
        boolean exists = dao.labelExists(subject1.getLabel(), subject1.getUserId());

        assertTrue(exists);
    }

    @Test
    void shouldNotDetectExistenceWhenLabelExistsForAnotherUser() {
        Long userId = addUserWithRoles().getId();

        boolean exists = dao.labelExists(subject1.getLabel(), userId);

        assertFalse(exists);
    }

    @Test
    void shouldNotDetectExistenceWhenLabelNotExists() {
        boolean exists = dao.labelExists(randAlph(11), subject1.getUserId());

        assertFalse(exists);
    }

    @Test
    void shouldDetectIfSubjectBelongsToUser() {
        boolean belongsToUser = dao.belongsToUser(userId, subject1.getId());

        assertTrue(belongsToUser);
    }


    @Test
    void shouldDetectIfSubjectDoesNotBelongToUser() {
        boolean belongsToUser = dao.belongsToUser(addUserWithRoles().getId(), subject1.getId());

        assertFalse(belongsToUser);
    }

}
