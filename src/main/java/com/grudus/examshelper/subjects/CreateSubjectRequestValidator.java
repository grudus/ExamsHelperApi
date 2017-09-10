package com.grudus.examshelper.subjects;

import com.grudus.examshelper.commons.keys.RestKeys;
import com.grudus.examshelper.exceptions.InvalidColorException;
import com.grudus.examshelper.users.auth.AuthenticationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class CreateSubjectRequestValidator implements Validator {

    private final SubjectService subjectService;
    private final AuthenticationService authenticationService;

    @Autowired
    public CreateSubjectRequestValidator(SubjectService subjectService, AuthenticationService authenticationService) {
        this.subjectService = subjectService;
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return SubjectDto.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SubjectDto dto = (SubjectDto) o;

        if (invalidLabel(dto.getLabel()))
            errors.reject(RestKeys.INVALID_LABEL);

        else if (labelExistsForUser(dto.getLabel()))
            errors.reject(RestKeys.LABEL_EXISTS);

        if (invalidColor(dto.getColor()))
            errors.reject(RestKeys.INVALID_COLOR);

    }

    private boolean invalidColor(String color) {
        try {
            Subject.assertColor(color);
        } catch (InvalidColorException e) {
            return true;
        }
        return false;
    }

    private boolean labelExistsForUser(String label) {
        return subjectService.labelExists(label, authenticationService.getCurrentLoggedUserId());
    }

    private boolean invalidLabel(String label) {
        return StringUtils.isBlank(label) || label.length() >= Subject.MAX_LABEL_LENGTH;
    }
}
