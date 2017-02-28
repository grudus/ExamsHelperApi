package com.grudus.examshelper.subjects;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
    public List<SubjectDto> getSubjects(AuthenticatedUser currentUser) {
        return subjectService.findByUser(currentUser.getUser().getId())
                .stream()
                .map(Subject::toDto)
                .collect(toList());
    }

    @PostMapping
    public void addSubject(@RequestBody SubjectDto subjectDto, AuthenticatedUser user) {
        subjectDto.setUserId(user.getUser().getId());
        subjectService.save(subjectDto.toSubject());
    }

    @PutMapping
    public void updateSubject(@RequestBody SubjectDto subject) {
        subjectService.update(subject.toSubject());
    }
}
