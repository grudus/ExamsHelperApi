package com.grudus.examshelper.subjects;

import com.grudus.examshelper.commons.IdResponse;
import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.exceptions.SubjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/subjects")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class SubjectController {

    private final SubjectService subjectService;
    private final CreateSubjectRequestValidator validator;
    private final SubjectSecurityService subjectSecurityService;

    @Autowired
    public SubjectController(SubjectService subjectService, CreateSubjectRequestValidator validator, SubjectSecurityService subjectSecurityService) {
        this.subjectService = subjectService;
        this.validator = validator;
        this.subjectSecurityService = subjectSecurityService;
    }

    @GetMapping
    public List<SubjectDto> getSubjects(AuthenticatedUser currentUser) {
        return subjectService.findByUser(currentUser.getUserId())
                .stream()
                .map(Subject::toDto)
                .collect(toList());
    }

    @GetMapping("/{id}")
    public SubjectDto findById(@PathVariable Long id, AuthenticatedUser user) {
        subjectSecurityService.assertSubjectBelongsToUser(user.getUserId(), id);
        return subjectService.findById(id)
                .orElseThrow(SubjectNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public IdResponse addSubject(@RequestBody @Valid SubjectDto subjectDto, AuthenticatedUser user) {
        Long id = subjectService.save(subjectDto.toSubject(user.getUserId()));
        return new IdResponse(id);
    }

    @PutMapping
    public void updateSubject(@RequestBody SubjectDto subject,  AuthenticatedUser user) {
        subjectService.update(subject.toSubject(user.getUserId()));
    }

    @DeleteMapping("/{id}")
    public void deleteSubject(@PathVariable Long id, AuthenticatedUser user) {
        subjectSecurityService.assertSubjectBelongsToUser(user.getUserId(), id);
        subjectService.delete(id);
    }


    @InitBinder("subjectDto")
    public void initValidator(WebDataBinder binder) {
        binder.addValidators(validator);
    }
}
