package com.grudus.examshelper.subjects;

import com.grudus.examshelper.AbstractControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.grudus.examshelper.users.roles.RoleName.USER;
import static com.grudus.examshelper.utils.Utils.*;
import static java.lang.String.format;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SubjectControllerTest extends AbstractControllerTest {

    private static final String SUBJECT_BASIC_URL = "/api/subjects";

    @Before
    public void addUser() {
        login(USER);
    }

    @Test
    public void shouldFindByLabel() throws Exception {
        Subject subject = randomSubject(authentication.getUserId());
        addSubject(subject);

        get(format("%s/%s", SUBJECT_BASIC_URL, subject.getLabel()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label", is(subject.getLabel())))
                .andExpect(jsonPath("$.color", is(subject.getColor())));
    }


    @Test
    public void shouldNotFindByLabel() throws Exception {
        get(format("%s/%s", SUBJECT_BASIC_URL, randAlph(11)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddSubject() throws Exception {
        Subject subject = randomSubject(authentication.getUserId());

        addSubject(subject);

        assertSubjectsSize(1)
                .andExpect(jsonPath("$[0].label", is(subject.getLabel())));
    }

    @Test
    public void shouldFindAllSubjects() throws Exception {
        Long userId = authentication.getUserId();
        Subject subject1 = randomSubject(userId), subject2 = randomSubject(userId);

        addSubject(subject1);
        addSubject(subject2);

        assertSubjectsSize(2)
                .andExpect(jsonPath("$[*].label", containsInAnyOrder(subject1.getLabel(), subject2.getLabel())))
                .andExpect(jsonPath("$[*].color", containsInAnyOrder(subject1.getColor(), subject2.getColor())));
    }

    @Test
    public void shouldUpdateSubject() throws Exception {
        Long userId = authentication.getUserId();
        Subject subject = randomSubject(userId);
        subject.setId(11L);

        addSubject(subject);

        put(SUBJECT_BASIC_URL, new SubjectDto(subject.getId(), randAlph(11), randomColor()))
                .andExpect(status().isOk());

        assertSubjectsSize(1)
                .andExpect(jsonPath("$[0].label", is(not(subject.getLabel()))))
                .andExpect(jsonPath("$[0].color", is(not(subject.getColor()))));
    }

    @Test
    public void shouldDeleteSubjectWhenIdInDb() throws Exception {
        Subject subject = randomSubject(authentication.getUserId());
        subject.setId(15L);
        addSubject(subject);
        addSubject(randomSubject(authentication.getUserId()));

        delete(format("%s/%d", SUBJECT_BASIC_URL, subject.getId()))
                .andExpect(status().isOk());

        assertSubjectsSize(1)
                .andExpect(jsonPath("$[0].label", is(not(subject.getLabel()))))
                .andExpect(jsonPath("$[0].color", is(not(subject.getColor()))));
    }

    @Test
    public void shouldDeleteNothingWhenIdNotInDb() throws Exception {
        addSubject(randomSubject(authentication.getUserId()));
        addSubject(randomSubject(authentication.getUserId()));

        delete(format("%s/%d", SUBJECT_BASIC_URL, 666L))
                .andExpect(status().isOk());

        assertSubjectsSize(2);
    }


    private void addSubject(Subject subject) throws Exception {
        post(SUBJECT_BASIC_URL, subject)
                .andExpect(status().isCreated());
    }

    private ResultActions assertSubjectsSize(int size) throws Exception {
        return get(SUBJECT_BASIC_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(size)));
    }
}
