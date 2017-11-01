package com.grudus.examshelper.exams;

import com.grudus.examshelper.AbstractControllerTest;
import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.exams.domain.CreateExamRequest;
import com.grudus.examshelper.subjects.SubjectController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.grudus.examshelper.users.roles.RoleName.USER;
import static com.grudus.examshelper.utils.RequestParam.param;
import static com.grudus.examshelper.utils.SubjectUtils.randomSubjectDto;
import static com.grudus.examshelper.utils.Utils.randAlph;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExamsSubjectControllerTest extends AbstractControllerTest {
    private static final String BASE_URL = "/api/exams";

    @Autowired
    private SubjectController subjectController;

    private Long subjectId;

    @BeforeEach
    void init() {
        login(USER);
        subjectId = addSubject();
        addSubject();
    }

    @Test
    void shouldFindExamsWithoutGradeForSubjectOnlyFromPast() throws Exception {
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().minusDays(4)));
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().minusDays(12)));
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().plusDays(3)));

        get(BASE_URL + "/without-grade", param("subjectId", subjectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    void shouldFindExamsWithoutGradeOnlyForGivenSubject() throws Exception {
        addExam(new CreateExamRequest(randAlph(11), addSubject(), now().minusDays(4)));
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().minusDays(12)));
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().minusDays(3)));

        get(BASE_URL + "/without-grade", param("subjectId", subjectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }


    @Test
    void shouldFindExamsWithoutGradeWhenNoSubject() throws Exception {
        addExam(new CreateExamRequest(randAlph(11), addSubject(), now().minusDays(4)));
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().minusDays(12)));
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().minusDays(3)));

        get(BASE_URL + "/without-grade")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)));

    }


    @Test
    void shouldReturn403WhenFindSomeoneElseExams() throws Exception {
        AuthenticatedUser user = new AuthenticatedUser(addUserWithRoles());
        Long otherId = addSubject(user);

        get(BASE_URL + "/without-grade", param("subjectId", otherId))
                .andExpect(status().isForbidden());
    }


    private void addExam(CreateExamRequest createExamRequest) throws Exception {
        post(ExamControllerTest.BASE_URL, createExamRequest);
    }

    private Long addSubject() {
        return addSubject(authentication);
    }

    private Long addSubject(AuthenticatedUser authenticatedUser) {
        return subjectController.addSubject(randomSubjectDto(), authenticatedUser)
                .getId();
    }
}