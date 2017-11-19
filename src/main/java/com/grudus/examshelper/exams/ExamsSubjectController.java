package com.grudus.examshelper.exams;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.exams.domain.ExamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamsSubjectController {

    private final ExamService examService;

    @Autowired
    public ExamsSubjectController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/without-grade")
    @PreAuthorize("@subjectSecurityService.hasAccessToSubject(#user, #subjectId)")
    public List<ExamDto> getExamsWithoutGrade(AuthenticatedUser user,
                                              @RequestParam(value = "subjectId", required = false) Long subjectId) {
        return examService.findWithoutGrade(user.getUserId(), subjectId);
    }
}
