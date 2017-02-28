package com.grudus.examshelper.subjects;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@PreAuthorize("hasAnyRole(ROLE_USER, ROLE_ADMIN)")
public class SubjectController {

    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Subject> getSubjects(AuthenticatedUser currentUser) {
        return subjectService.findByUser(currentUser.getUser().getId());
    }

    @PutMapping
    public void updateSubject(@RequestBody Subject subject) {
        subjectService.update(subject);
    }
}
