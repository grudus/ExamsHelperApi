package com.grudus.examshelper.stats;

import com.grudus.examshelper.AbstractControllerTest;
import com.grudus.examshelper.exams.ExamController;
import com.grudus.examshelper.exams.domain.CreateExamRequest;
import com.grudus.examshelper.subjects.SubjectController;
import com.grudus.examshelper.users.roles.RoleName;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.grudus.examshelper.users.roles.RoleName.USER;
import static com.grudus.examshelper.utils.RequestParam.param;
import static com.grudus.examshelper.utils.SubjectUtils.randomSubjectDto;
import static com.grudus.examshelper.utils.Utils.randAlph;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SubjectStatsControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/stats";

    @Autowired
    private SubjectController subjectController;

    @Autowired
    private ExamController examController;

    private final LocalDateTime NOW = now();
    private Long subjectId;

    @Before
    public void init() {
        login(USER);
        subjectId = addSubject();
    }

    @Test
    public void shouldGetAverageExamsPerMonth() throws Exception {
        addExamWithGrade(subjectId, NOW, 4.0);
        addExamWithGrade(subjectId, NOW, 5.0);
        addExamWithGrade(subjectId, NOW, 6.0);

        get(BASE_URL + "/average", param("subjectId", subjectId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].averageGrade").value(5.0));
    }

    @Test
    public void shouldNotBeAbleToGetSomeoneElseSubjectStats() throws Exception {
        login(RoleName.ADMIN);
        Long subjectId = addSubject();
        addExamWithGrade(subjectId, NOW, 4.0);
        login(RoleName.USER);

        get(BASE_URL + "/average", param("subjectId", subjectId.toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldGetAverageGradesForAllSubjectsWhenNoSubjectIdSpecified() throws Exception {
        addExamWithGrade(subjectId, NOW, 6.0);
        addExamWithGrade(addSubject(), NOW, 3.0);
        addExamWithGrade(addSubject(), NOW, 3.0);

        get(BASE_URL + "/average")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].averageGrade").value(4.0));
    }

    @Test
    public void shouldGetExamsPerMonth() throws Exception {
        addExamWithGrade(subjectId, NOW, 3.0);
        addExamWithGrade(subjectId, NOW.plusMonths(2), 3.0);
        addExamWithGrade(subjectId, NOW.minusMonths(1), 3.0);
        addExamWithGrade(subjectId, NOW.plusYears(2), 3.0);
        addExamWithGrade(subjectId, NOW.plusYears(2).plusMonths(2).plusDays(3), 3.0);

        get(BASE_URL + "/average", param("subjectId", subjectId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)));
    }

    @Test
    public void shouldReturnEmptyListWhenNoGradesForSubject() throws Exception {
        get(BASE_URL + "/average", param("subjectId", subjectId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", emptyCollectionOf(AverageExamsGradePerMonth.class)));
    }

    private void addExamWithGrade(Long subjectId, LocalDateTime date, double grade) {
        Long examId = examController.createExam(new CreateExamRequest(randAlph(11), subjectId, date)).getId();
        examController.addGrade(authentication, examId, grade);
    }


    private Long addSubject() {
        return subjectController.addSubject(randomSubjectDto(), authentication).getId();
    }
}