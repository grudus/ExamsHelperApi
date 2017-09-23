package com.grudus.examshelper.stats;

import com.grudus.examshelper.commons.Pair;
import com.grudus.examshelper.exams.ExamService;
import com.grudus.examshelper.exams.domain.ExamDto;
import com.grudus.examshelper.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class StatsService {

    private final ExamService examService;

    @Autowired
    public StatsService(ExamService examService) {
        this.examService = examService;
    }

    List<AverageExamsGradePerMonth> getAverageExamsGradePerMonth(User user, Long subjectId) {
        List<ExamDto> allExams = examService.findAllExamsAsDtoByUser(user);

        Map<Pair<Integer, Month>, List<ExamDto>> examsPerMonth = allExams.stream()
                .filter(ExamDto::hasGrade)
                .filter(exam -> filterBySubject(exam, subjectId))
                .collect(groupingBy(i -> Pair.of(i.getDate().getYear(), new Month(i.getDate()))));

        return examsPerMonth.entrySet().stream()
                .map(this::mapToAverageExamsGradePerMonth)
                .collect(toList());

    }


    private AverageExamsGradePerMonth mapToAverageExamsGradePerMonth(Map.Entry<Pair<Integer, Month>, List<ExamDto>> dateToExams) {
        return new AverageExamsGradePerMonth(
                dateToExams.getKey().getFirst(),
                dateToExams.getKey().getSecond(),
                calculateAverage(dateToExams.getValue())
        );
    }

    private Double calculateAverage(List<ExamDto> exams) {
        return exams.stream().mapToDouble(ExamDto::getGrade)
                .average()
                .orElse(0D);
    }

    private boolean filterBySubject(ExamDto exam, Long subjectId) {
        return subjectId == null ? true :
                exam.getSubjectId()
                        .map(id -> id.equals(subjectId))
                        .orElse(false);
    }
}
