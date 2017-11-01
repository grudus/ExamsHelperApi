package com.grudus.examshelper.exams;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.exams.domain.ExamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subjects/{subjectId}/exams")
public class SubjectExamsController {

    private final ExamService examService;

    @Autowired
    public SubjectExamsController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/without-grade")
    @PreAuthorize("@subjectSecurityService.hasAccessToSubject(#user, #subjectId)")
    public List<ExamDto> getExamsWithoutGrade(AuthenticatedUser user, @PathVariable("subjectId") Long subjectId) {
        return examService.findWithoutGradeForSubject(subjectId);
    }
}
