package com.grudus.examshelper.exams;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.exams.domain.ExamDto;
import com.grudus.examshelper.subjects.SubjectSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subjects/{subjectId}/exams")
public class SubjectExamsController {

    private final ExamService examService;
    private final SubjectSecurityService subjectSecurityService;

    @Autowired
    public SubjectExamsController(ExamService examService, SubjectSecurityService subjectSecurityService) {
        this.examService = examService;
        this.subjectSecurityService = subjectSecurityService;
    }

    @GetMapping("/without-grade")
    public List<ExamDto> getExamsWithoutGrade(AuthenticatedUser user, @PathVariable("subjectId") Long subjectId) {
        subjectSecurityService.assertSubjectBelongsToUser(user.getUserId(), subjectId);
        return examService.findWithoutGradeForSubject(subjectId);
    }
}
