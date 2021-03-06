package com.grudus.examshelper.exams;

import com.grudus.examshelper.MockitoExtension;
import com.grudus.examshelper.exams.domain.CreateExamRequest;
import com.grudus.examshelper.exams.domain.Exam;
import com.grudus.examshelper.exams.domain.ExamDto;
import com.grudus.examshelper.exams.domain.ExamsPerDay;
import com.grudus.examshelper.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static com.grudus.examshelper.utils.Utils.randAlph;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExamServiceTest {

    @Mock
    private ExamDao dao;

    @InjectMocks
    private ExamService service;

    @Test
    void shouldFindAllExamsPerDay() throws Exception {
        LocalDateTime now = now();
        List<ExamDto> exams = asList(randomAtDate(now), randomAtDate(now), randomAtDate(now),
                randomAtDate(now.plusDays(6)), randomAtDate(now.plusDays(6)),
                randomAtDate(now.plusDays(11)));

        when(dao.findAllAsExams(anyLong())).thenReturn(exams);

        List<ExamsPerDay> examsPerDay = service.findAllExamsPerDay(new User())
                .stream().sorted(comparing(ExamsPerDay::getDate))
                .collect(toList());

        assertEquals(3, examsPerDay.size());

        assertEquals(3, examsPerDay.get(0).getExams().size());
        assertEquals(2, examsPerDay.get(1).getExams().size());
        assertEquals(1, examsPerDay.get(2).getExams().size());
    }

    @Test
    void shouldFindAllPerDayFromDate() {
        LocalDateTime now = now();
        List<ExamDto> exams = asList(
                randomAtDate(now.plusDays(6)), randomAtDate(now.plusDays(6)),
                randomAtDate(now.plusDays(11)));

        when(dao.findAllFromDate(anyLong(), any())).thenReturn(exams);

        List<ExamsPerDay> examsPerDay = service.findAllExamsPerDay(new User(), now);

        assertEquals(2, examsPerDay.size());
        assertEquals(1, examsPerDay.get(0).getExams().size());
        assertEquals(2, examsPerDay.get(1).getExams().size());

    }

    @Test
    void shouldMapToExamAndSave() throws Exception {
        CreateExamRequest request = new CreateExamRequest(randAlph(11), new Random().nextLong(), now());

        ArgumentCaptor<Exam> capturedExam = ArgumentCaptor.forClass(Exam.class);
        service.save(request);

        verify(dao).save(capturedExam.capture());

        Exam exam = capturedExam.getValue();
        assertEquals(request.getSubjectId(), exam.getSubjectId());
        assertEquals(request.getDate(), exam.getDate());
        assertEquals(request.getInfo(), exam.getInfo());
    }

    private ExamDto randomAtDate(LocalDateTime dateTime) {
        return new ExamDto(randAlph(11), dateTime, -1D, null);
    }

}