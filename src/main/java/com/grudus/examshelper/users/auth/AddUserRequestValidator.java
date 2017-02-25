package com.grudus.examshelper.users.auth;

import com.grudus.examshelper.users.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.grudus.examshelper.commons.keys.RestKeys.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class AddUserRequestValidator implements Validator {

    private final UserService userService;
    private final EmailValidator emailValidator;

    @Autowired
    public AddUserRequestValidator(UserService userService, EmailValidator emailValidator) {
        this.userService = userService;
        this.emailValidator = emailValidator;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return AddUserRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AddUserRequest request = (AddUserRequest) o;

        if (isBlank(request.getUsername()))
            errors.reject(EMPTY_USERNAME);

        else if (userService.findByUsername(request.getUsername()).isPresent())
            errors.reject(USERNAME_EXISTS);

        if (isBlank(request.getPassword()))
            errors.reject(EMPTY_PASSWORD);

        if (isBlank(request.getEmail()))
            errors.reject(EMPTY_EMAIL);

        else if (!emailValidator.isValid(request.getEmail()))
            errors.reject(INVALID_EMAIL);

        else if (userService.findByEmail(request.getEmail()).isPresent())
            errors.reject(EMAIL_EXISTS);
    }
}
