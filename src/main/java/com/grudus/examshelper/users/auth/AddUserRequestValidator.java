package com.grudus.examshelper.users.auth;

import com.grudus.examshelper.users.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.springframework.util.ObjectUtils.isEmpty;

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

        if (isEmpty(request.getUsername()))
            errors.reject("Username cannot be empty");
        if (isEmpty(request.getPassword()))
            errors.reject("Password cannot be empty");

        String email = request.getEmail();

        if (!emailValidator.isValid(email))
            errors.reject("Email is not valid");

        else if (userService.findByEmail(email).isPresent())
            errors.reject("Email already exists");

        if (userService.findEnabledByUsername(request.getUsername()).isPresent())
            errors.reject("Username already exists");

    }
}
