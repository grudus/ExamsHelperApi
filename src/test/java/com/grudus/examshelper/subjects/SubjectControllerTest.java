package com.grudus.examshelper.subjects;

import com.grudus.examshelper.AbstractControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.grudus.examshelper.Utils.*;
import static com.grudus.examshelper.users.roles.RoleName.USER;
import static java.lang.String.format;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        Subject subject = randomSubject(authentication.getUser().getId());
        addSubject(subject);

        performRequestWithAuth(get(format("%s/%s", SUBJECT_BASIC_URL, subject.getLabel())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label", is(subject.getLabel())))
                .andExpect(jsonPath("$.color", is(subject.getColor())));
    }


    @Test
    public void shouldNotFindByLabel() throws Exception {
        performRequestWithAuth(get(format("%s/%s", SUBJECT_BASIC_URL, randAlph(11))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddSubject() throws Exception {
        Subject subject = randomSubject(authentication.getUser().getId());

        addSubject(subject);

        assertSubjectsSize(1)
                .andExpect(jsonPath("$[0].label", is(subject.getLabel())));
    }

    @Test
    public void shouldFindAllSubjects() throws Exception {
        Long userId = authentication.getUser().getId();
        Subject subject1 = randomSubject(userId), subject2 = randomSubject(userId);

        addSubject(subject1);
        addSubject(subject2);

        assertSubjectsSize(2)
                .andExpect(jsonPath("$[*].label", containsInAnyOrder(subject1.getLabel(), subject2.getLabel())))
                .andExpect(jsonPath("$[*].color", containsInAnyOrder(subject1.getColor(), subject2.getColor())));
    }

    @Test
    public void shouldUpdateSubject() throws Exception {
        Long userId = authentication.getUser().getId();
        Subject subject = randomSubject(userId);
        subject.setId(11L);

        addSubject(subject);

        performRequestWithAuth(put(SUBJECT_BASIC_URL)
                .contentType(APPLICATION_JSON)
                .content(toJson(new SubjectDto(subject.getId(), randAlph(11), randomColor()))))
                .andExpect(status().isOk());

        assertSubjectsSize(1)
                .andExpect(jsonPath("$[0].label", is(not(subject.getLabel()))))
                .andExpect(jsonPath("$[0].color", is(not(subject.getColor()))));
    }

    @Test
    public void shouldDeleteSubjectWhenIdInDb() throws Exception {
        Subject subject = randomSubject(authentication.getUser().getId());
        subject.setId(15L);
        addSubject(subject);
        addSubject(randomSubject(authentication.getUser().getId()));

        performRequestWithAuth(delete(format("%s/%d", SUBJECT_BASIC_URL, subject.getId())))
                .andExpect(status().isOk());

        assertSubjectsSize(1)
                .andExpect(jsonPath("$[0].label", is(not(subject.getLabel()))))
                .andExpect(jsonPath("$[0].color", is(not(subject.getColor()))));
    }

    @Test
    public void shouldDeleteNothingWhenIdNotInDb() throws Exception {
        addSubject(randomSubject(authentication.getUser().getId()));
        addSubject(randomSubject(authentication.getUser().getId()));

        performRequestWithAuth(delete(format("%s/%d", SUBJECT_BASIC_URL, 666L)))
                .andExpect(status().isOk());

        assertSubjectsSize(2);
    }


    private void addSubject(Subject subject) throws Exception {
        performRequestWithAuth(post(SUBJECT_BASIC_URL)
                .contentType(APPLICATION_JSON)
                .content(toJson(subject)))
                .andExpect(status().isOk());
    }

    private ResultActions assertSubjectsSize(int size) throws Exception {
        return performRequestWithAuth(get(SUBJECT_BASIC_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(size)));
    }
}
