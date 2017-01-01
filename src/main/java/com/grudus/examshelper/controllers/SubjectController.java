package com.grudus.examshelper.controllers;

import com.grudus.examshelper.configuration.authenticated.AuthenticatedUser;
import com.grudus.examshelper.domain.Subject;
import com.grudus.examshelper.exceptions.SubjectNotFoundException;
import com.grudus.examshelper.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteAllSubjects(AuthenticatedUser currentUser) {
        subjectService.deleteAll(currentUser.getUser().getSubjectList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Subject getSubject(AuthenticatedUser currentUser, @PathVariable("id") Long id) {
        return subjectService.findByUserAndId(currentUser.getUser().getId(), id)
                .orElseThrow(SubjectNotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addSubject(AuthenticatedUser currentUser, Subject subject) {
        subjectService.save(subject);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void removeSubject(AuthenticatedUser currentUser, @PathVariable("id") Long id) {
        subjectService.deleteByUserIdAndId(currentUser.getUser().getId(), id);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public void updateSubject(AuthenticatedUser currentUser, @PathVariable("id") Long id, Subject subject) {
        subjectService.update(subject);
    }
}
