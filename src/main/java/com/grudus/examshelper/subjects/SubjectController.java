package com.grudus.examshelper.subjects;

import com.grudus.examshelper.commons.IdResponse;
import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.exceptions.SubjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/subjects")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class SubjectController {

    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<SubjectDto> getSubjects(AuthenticatedUser currentUser) {
        return subjectService.findByUser(currentUser.getUser().getId())
                .stream()
                .map(Subject::toDto)
                .collect(toList());
    }

    @GetMapping("/{label}")
    public SubjectDto findByLabel(@PathVariable String label, AuthenticatedUser user) {
        return subjectService.findByLabel(user.getUser().getId(), label)
                .orElseThrow(SubjectNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public IdResponse addSubject(@RequestBody SubjectDto subjectDto, AuthenticatedUser user) {
        Long id = subjectService.save(subjectDto.toSubject(user.getUser().getId()));
        return new IdResponse(id);
    }

    @PutMapping
    public void updateSubject(@RequestBody SubjectDto subject,  AuthenticatedUser user) {
        subjectService.update(subject.toSubject(user.getUser().getId()));
    }

    @DeleteMapping("/{id}")
    public void deleteSubject(@PathVariable Long id) {
        subjectService.delete(id);
    }
}
