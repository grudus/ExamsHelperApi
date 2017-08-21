package com.grudus.examshelper.exams;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exams")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class ExamController {

    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping
    public List<ExamDto> getAllExams(AuthenticatedUser user) {
        return examService.findAllExamsAsDtoByUser(user.getUser());
    }

    @GetMapping("/day")
    public Map<LocalDate, List<ExamDto>> getAllExamsPerDay(AuthenticatedUser user) {
        return examService.findAllExamsPerDay(user.getUser());
    }

    @PostMapping
    public void createExam(@RequestBody CreateExamRequest createExamRequest) {
        examService.save(createExamRequest);
    }
}
