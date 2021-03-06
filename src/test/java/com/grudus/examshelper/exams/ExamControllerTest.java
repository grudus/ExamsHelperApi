package com.grudus.examshelper.exams;

import com.grudus.examshelper.AbstractControllerTest;
import com.grudus.examshelper.commons.IdResponse;
import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.exams.domain.CreateExamRequest;
import com.grudus.examshelper.subjects.SubjectController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

import static com.grudus.examshelper.users.roles.RoleName.USER;
import static com.grudus.examshelper.utils.RequestParam.param;
import static com.grudus.examshelper.utils.SubjectUtils.randomSubjectDto;
import static com.grudus.examshelper.utils.Utils.randAlph;
import static com.grudus.examshelper.utils.Utils.randomId;
import static java.time.LocalDateTime.now;
import static java.util.stream.IntStream.range;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExamControllerTest extends AbstractControllerTest {

    static final String BASE_URL = "/api/exams";

    @Autowired
    private SubjectController subjectController;

    private Long subjectId;

    @BeforeEach
    void init() {
        login(USER);
        subjectId = addSubject();
    }

    @Test
    void shouldCreateExam() throws Exception {
        CreateExamRequest request = new CreateExamRequest(randAlph(11), subjectId, now());

        post(BASE_URL, request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void shouldCreateExamWithoutInfo() throws Exception {
        CreateExamRequest request = new CreateExamRequest(null, subjectId, now());

        post(BASE_URL, request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void shouldCreateExamInPast() throws Exception {
        CreateExamRequest request = new CreateExamRequest(null, subjectId, now().minusDays(11));

        post(BASE_URL, request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void shouldNotCreateExamWithoutSubjectId() throws Exception {
        CreateExamRequest request = new CreateExamRequest(randAlph(11), null, now());

        post(BASE_URL, request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotCreateExamWithInvalidSubjectId() throws Exception {
        CreateExamRequest request = new CreateExamRequest(randAlph(11), new Random().nextLong(), now());

        post(BASE_URL, request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotCreateExamWithoutDate() throws Exception {
        CreateExamRequest request = new CreateExamRequest(randAlph(11), subjectId, null);

        post(BASE_URL, request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllExams() throws Exception {
        Long[] ids = {subjectId, addSubject()};
        range(0, 10).mapToObj(i -> new CreateExamRequest(randAlph(11), ids[i % 2], now()))
                .forEach(this::addExam);

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(10)));
    }

    @Test
    void shouldReturnEmptyListWhenNoExams() throws Exception {
        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    void shouldGetAllExamsPerDay() throws Exception {
        Long[] ids = {subjectId, addSubject()};
        range(0, 10).mapToObj(i -> new CreateExamRequest(randAlph(11), ids[i % 2], now().plusDays(i % 3)))
                .forEach(this::addExam);

        get(BASE_URL + "/day")
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.[2].exams", hasSize(4)))
                .andExpect(jsonPath("$.[1].exams", hasSize(3)))
                .andExpect(jsonPath("$.[0].exams", hasSize(3)));
    }

    @Test
    void shouldGetAllExamsPerDayFromDate() throws Exception {
        Long[] ids = {subjectId, addSubject()};
        range(0, 10).mapToObj(i -> new CreateExamRequest(randAlph(11), ids[i % 2], now().plusDays(i)))
                .forEach(this::addExam);
        get(BASE_URL + "/day", param("dateFrom", now().plusDays(5).minusHours(1).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)));
    }

    @Test
    void shouldCountExamsWithoutGrade() throws Exception {
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().plusDays(4)));
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().minusDays(4)));
        addExam(new CreateExamRequest(randAlph(11), addSubject(), now().minusDays(1)));

        get(BASE_URL + "/without-grade/count")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(2)));
    }

    @Test
    void shouldCountNoExamsWithoutGrade() throws Exception {
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().plusDays(4)));
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().plusDays(4)));

        get(BASE_URL + "/without-grade/count")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(0)));
    }

    @Test
    void shouldUpdateGrade() throws Exception {
        Long examId = addExam(new CreateExamRequest(randAlph(11), subjectId, now()));
        Double grade = 66D;

        putWithParams(BASE_URL + "/" + examId, param("grade", grade.toString()))
                .andExpect(status().isOk());

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].grade").value(grade));
    }

    @Test
    void shouldUpdateGradeToNull() throws Exception {
        Long examId = addExam();
        Double grade = 66D;

        putWithParams(BASE_URL + "/" + examId, param("grade", grade.toString()))
                .andExpect(status().isOk());
        putWithParams(BASE_URL + "/" + examId)
                .andExpect(status().isOk());

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].grade", nullValue()));
    }

    private long addExam() {
        return addExam(new CreateExamRequest(randAlph(11), subjectId, now()));
    }

    @Test
    void shouldNotBeAbleToUpdateSomeoneElseExam() throws Exception {
        Long subjectId = addSubject(new AuthenticatedUser(addUserWithRoles()));
        Long examId = addExam(new CreateExamRequest(randAlph(11), subjectId, now()));

        putWithParams(BASE_URL + "/" + examId, param("grade", "33.0"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldDeleteExam() throws Exception {
        Long examId = addExam(new CreateExamRequest(randAlph(11), subjectId, now()));

        delete(BASE_URL + "/" + examId)
                .andExpect(status().isOk());

        get(BASE_URL)
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    void shouldNotBeAbleToDeleteSomeoneElseExam() throws Exception {
        Long subjectId = addSubject(new AuthenticatedUser(addUserWithRoles()));
        Long examId = addExam(new CreateExamRequest(randAlph(11), subjectId, now()));

        delete(BASE_URL + "/" + examId)
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldDeleteNothingWhenInvalidId() throws Exception {
        addExam(new CreateExamRequest(randAlph(11), subjectId, now()));

        delete(BASE_URL + "/" + randomId())
                .andExpect(status().isOk());

        get(BASE_URL)
                .andExpect(jsonPath("$.*", hasSize(1)));

    }


    private long addExam(CreateExamRequest createExamRequest) {
        try {
            return post(BASE_URL, createExamRequest, IdResponse.class).getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Long addSubject() {
        return addSubject(authentication);
    }

    private Long addSubject(AuthenticatedUser authenticatedUser) {
        return subjectController.addSubject(randomSubjectDto(), authenticatedUser)
                .getId();
    }

}