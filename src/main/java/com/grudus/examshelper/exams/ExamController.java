package com.grudus.examshelper.exams;

import com.grudus.examshelper.commons.IdResponse;
import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.exams.domain.CreateExamRequest;
import com.grudus.examshelper.exams.domain.ExamDto;
import com.grudus.examshelper.exams.domain.ExamsPerDay;
import com.grudus.examshelper.exams.domain.NotGradedExamsCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/exams")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class ExamController {

    private final ExamService examService;
    private final CreateExamRequestValidator validator;

    @Autowired
    public ExamController(ExamService examService, CreateExamRequestValidator validator) {
        this.examService = examService;
        this.validator = validator;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public IdResponse createExam(@Valid @RequestBody CreateExamRequest createExamRequest) {
        return new IdResponse(examService.save(createExamRequest));
    }

    @GetMapping
    public List<ExamDto> getAllExams(AuthenticatedUser user) {
        return examService.findAllExamsAsDtoByUser(user.getUser());
    }

    @GetMapping("/day")
    public List<ExamsPerDay> getAllExamsPerDay(AuthenticatedUser user,
                                               @DateTimeFormat(iso = DATE_TIME) @RequestParam(required = false) LocalDateTime dateFrom) {
        return examService.findAllExamsPerDay(user.getUser(), dateFrom);
    }

    @GetMapping("/without-grade")
    public NotGradedExamsCount getNumberOfNotGradedExams(AuthenticatedUser user) {
        return new NotGradedExamsCount(examService.countNotGraded(user.getUser()));
    }

    @PutMapping("/{examId}")
    @PreAuthorize("@examsSecurityService.hasAccessToExam(#user, #examId)")
    public void addGrade(AuthenticatedUser user, @PathVariable("examId") Long examId,
                         @RequestParam(value = "grade", required = false) Double grade) {
        examService.updateGrade(examId, grade);
    }

    @InitBinder("createExamRequest")
    public void initValidator(WebDataBinder binder) {
        binder.addValidators(validator);
    }

}
