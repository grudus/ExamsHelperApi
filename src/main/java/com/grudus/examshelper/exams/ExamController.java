package com.grudus.examshelper.exams;

import com.grudus.examshelper.commons.IdResponse;
import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public List<ExamsPerDay> getAllExamsPerDay(AuthenticatedUser user) {
        return examService.findAllExamsPerDay(user.getUser());
    }


    @InitBinder("createExamRequest")
    public void initValidator(WebDataBinder binder) {
        binder.addValidators(validator);
    }
}
