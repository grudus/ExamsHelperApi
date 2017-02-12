package com.grudus.examshelper.users.auth;

import com.grudus.examshelper.users.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.springframework.validation.ValidationUtils.rejectIfEmpty;

@Component
public class AddUserRequestValidator implements Validator {

    private final UserService userService;
    private final EmailValidator emailValidator;

    @Autowired
    public AddUserRequestValidator(UserService userService) {
        this.userService = userService;
        emailValidator = EmailValidator.getInstance();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return AddUserRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AddUserRequest request = (AddUserRequest) o;

        rejectIfEmpty(errors, "username", "400");
        rejectIfEmpty(errors, "password", "401");

        String email = request.getEmail();
        if (!emailValidator.isValid(email))
            errors.reject("Email is not valid");

        else if (userService.findByEmail(email).isPresent())
            errors.reject("Email already exists");

        if (userService.findByUsername(request.getUsername()).isPresent())
            errors.reject("Username already exists");

    }
}
