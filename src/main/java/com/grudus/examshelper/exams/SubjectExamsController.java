package com.grudus.examshelper.exams;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.exams.domain.ExamDto;
import com.grudus.examshelper.exceptions.IllegalActionException;
import com.grudus.examshelper.subjects.SubjectService;
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
    private final SubjectService subjectService;

    @Autowired
    public SubjectExamsController(ExamService examService, SubjectService subjectService) {
        this.examService = examService;
        this.subjectService = subjectService;
    }

    @GetMapping("/without-grade")
    public List<ExamDto> getExamsWithoutGrade(AuthenticatedUser user, @PathVariable("subjectId") Long subjectId) {
        assertSubjectBelongsToUser(user.getUserId(), subjectId);
        return examService.findWithoutGradeForSubject(subjectId);
    }

    private void assertSubjectBelongsToUser(Long userId, Long subjectId) {
        if (!subjectService.belongsToUser(userId, subjectId))
            throw new IllegalActionException("User {%d} tries to steal someone else's subject {%d}!", userId, subjectId);
    }
}
