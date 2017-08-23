package com.grudus.examshelper.exams;

import com.grudus.examshelper.commons.keys.RestKeys;
import com.grudus.examshelper.subjects.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CreateExamRequestValidator implements Validator {

    private final SubjectService subjectService;

    @Autowired
    public CreateExamRequestValidator(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CreateExamRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CreateExamRequest request = (CreateExamRequest) o;

        if (invalidSubject(request.getSubjectId()))
            errors.reject(RestKeys.INVALID_SUBJECT);
        if (request.getDate() == null)
            errors.reject(RestKeys.EMPTY_DATE);
    }

    private boolean invalidSubject(Long subjectId) {
        return subjectId == null || !subjectService.exists(subjectId);
    }
}
