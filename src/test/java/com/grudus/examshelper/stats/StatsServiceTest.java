package com.grudus.examshelper.stats;

import com.grudus.examshelper.MockitoExtension;
import com.grudus.examshelper.exams.ExamService;
import com.grudus.examshelper.exams.domain.ExamDto;
import com.grudus.examshelper.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;

import static com.grudus.examshelper.utils.ExamUtils.randomExamDto;
import static com.grudus.examshelper.utils.Utils.randomUser;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatsServiceTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

    @Mock
    private ExamService examService;

    @InjectMocks
    private StatsService statsService;

    @Test
    public void shouldGetAverageGradeForMonth() {
        returnWhenFindingExams(
                randomExamDto(NOW, 5.0),
                randomExamDto(NOW, 4.0),
                randomExamDto(NOW, 3.5));

        Double average = statsService.getAverageExamsGradePerMonth(randomUser(), null)
                .get(0).getAverageGrade();

        assertEquals(12.5 / 3.0, average, 0.05);
    }

    @Test
    public void shouldGetAverageForManyMonths() {
        returnWhenFindingExams(
                randomExamDto(NOW, 5.0),
                randomExamDto(NOW, 4.0),
                randomExamDto(NOW.plusMonths(1), 3.0),
                randomExamDto(NOW.minusMonths(2), 1.0)
        );

        List<AverageExamsGradePerMonth> averages = statsService.getAverageExamsGradePerMonth(randomUser(), null);

        assertEquals(3, averages.size());
    }

    @Test
    public void shouldDetectDifferentYears() {
        returnWhenFindingExams(
                randomExamDto(NOW, 5.0),
                randomExamDto(NOW.plusYears(1), 5.0)
        );

        List<AverageExamsGradePerMonth> averages = statsService.getAverageExamsGradePerMonth(randomUser(), null);

        assertEquals(2, averages.size());
    }

    @Test
    public void shouldFindOnlyForGivenSubject() {
        returnWhenFindingExams(
                randomExamDto(NOW, 4.0, 1L),
                randomExamDto(NOW, 4.0, 2L)
        );

        List<AverageExamsGradePerMonth> averages = statsService.getAverageExamsGradePerMonth(randomUser(), 2L);

        assertEquals(1, averages.size());
    }

    @Test
    public void shouldReturnEmptyListWhenNoExams() {
        returnWhenFindingExams();

        List<AverageExamsGradePerMonth> averages = statsService.getAverageExamsGradePerMonth(randomUser(), 2L);
        assertTrue(averages.isEmpty());
    }


    @Test
    public void shouldReturnEmptyListWhenNoExamsWithGrade() {
        returnWhenFindingExams(
                randomExamDto(NOW, null),
                randomExamDto(NOW.plusYears(1), 0D),
                randomExamDto(NOW.plusYears(1), -1D)
        );

        List<AverageExamsGradePerMonth> averages = statsService.getAverageExamsGradePerMonth(randomUser(), 2L);
        assertTrue(averages.isEmpty());
    }


    @Test
    public void shouldReturnEmptyListWhenNoExamsForSubject() {
        returnWhenFindingExams(
                randomExamDto(NOW, 5.0, 2L),
                randomExamDto(NOW.plusYears(1), 0D, 3L)
        );

        List<AverageExamsGradePerMonth> averages = statsService.getAverageExamsGradePerMonth(randomUser(), 1L);
        assertTrue(averages.isEmpty());
    }


    private void returnWhenFindingExams(ExamDto... exams) {
        when(examService.findAllExamsAsDtoByUser(any(User.class))).thenReturn(
                asList(exams)
        );
    }

}