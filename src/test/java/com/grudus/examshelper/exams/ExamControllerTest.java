package com.grudus.examshelper.exams;

import com.grudus.examshelper.AbstractControllerTest;
import com.grudus.examshelper.exams.domain.CreateExamRequest;
import com.grudus.examshelper.subjects.SubjectController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

import static com.grudus.examshelper.users.roles.RoleName.USER;
import static com.grudus.examshelper.utils.RequestParam.param;
import static com.grudus.examshelper.utils.SubjectUtils.randomSubjectDto;
import static com.grudus.examshelper.utils.Utils.randAlph;
import static java.time.LocalDateTime.now;
import static java.util.stream.IntStream.range;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExamControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/exams";

    @Autowired
    private SubjectController subjectController;

    private Long subjectId;

    @Before
    public void init() {
        login(USER);
        subjectId = addSubject();
    }

    @Test
    public void shouldCreateExam() throws Exception {
        CreateExamRequest request = new CreateExamRequest(randAlph(11), subjectId, now());

        post(BASE_URL, request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    public void shouldCreateExamWithoutInfo() throws Exception {
        CreateExamRequest request = new CreateExamRequest(null, subjectId, now());

        post(BASE_URL, request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    public void shouldCreateExamInPast() throws Exception {
        CreateExamRequest request = new CreateExamRequest(null, subjectId, now().minusDays(11));

        post(BASE_URL, request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    public void shouldNotCreateExamWithoutSubjectId() throws Exception {
        CreateExamRequest request = new CreateExamRequest(randAlph(11), null, now());

        post(BASE_URL, request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateExamWithInvalidSubjectId() throws Exception {
        CreateExamRequest request = new CreateExamRequest(randAlph(11), new Random().nextLong(), now());

        post(BASE_URL, request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateExamWithoutDate() throws Exception {
        CreateExamRequest request = new CreateExamRequest(randAlph(11), subjectId, null);

        post(BASE_URL, request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetAllExams() throws Exception {
        Long[] ids = {subjectId, addSubject()};
        range(0, 10).mapToObj(i -> new CreateExamRequest(randAlph(11), ids[i % 2], now()))
                .forEach(this::addExam);

        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(10)));
    }

    @Test
    public void shouldReturnEmptyListWhenNoExams() throws Exception {
        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    public void shouldGetAllExamsPerDay() throws Exception {
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
    public void shouldGetAllExamsPerDayFromDate() throws Exception {
        Long[] ids = {subjectId, addSubject()};
        range(0, 10).mapToObj(i -> new CreateExamRequest(randAlph(11), ids[i % 2], now().plusDays(i)))
                .forEach(this::addExam);
        get(BASE_URL + "/day", param("dateFrom", now().plusDays(5).minusHours(1).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)));
    }

    @Test
    public void shouldCountExamsWithoutGrade() throws Exception {
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().plusDays(4)));
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().minusDays(4)));
        addExam(new CreateExamRequest(randAlph(11), addSubject(), now().minusDays(1)));

        get(BASE_URL + "/without-grade")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(2)));
    }

    @Test
    public void shouldCountNoExamsWithoutGrade() throws Exception {
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().plusDays(4)));
        addExam(new CreateExamRequest(randAlph(11), subjectId, now().plusDays(4)));

        get(BASE_URL + "/without-grade")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(0)));
    }


    private void addExam(CreateExamRequest createExamRequest) {
        try {
            post(BASE_URL, createExamRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Long addSubject() {
        return subjectController.addSubject(randomSubjectDto(), authentication)
                .getId();
    }

}