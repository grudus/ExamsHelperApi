package com.grudus.examshelper.users.auth;

import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Optional;

import static com.grudus.examshelper.Utils.randAlph;
import static com.grudus.examshelper.Utils.randomEmail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddUserRequestValidatorTest {

    @Mock
    private EmailValidator emailValidator;

    @Mock
    private UserService userService;

    private AddUserRequestValidator validator;

    private AddUserRequest validatedObject;
    private Errors errors;

    @Before
    public void init() {
        validatedObject = new AddUserRequest(randAlph(10), randAlph(10), randomEmail());
        errors = new BeanPropertyBindingResult(validatedObject, "addUserRequest");
        validator = new AddUserRequestValidator(userService, emailValidator);

        when(emailValidator.isValid(anyString())).thenReturn(true);
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
    }

    @Test
    public void shouldValidateProperly() {
        validator.validate(validatedObject, errors);
        assertEquals(0, errors.getErrorCount());
    }

    @Test
    public void shouldNotPassValidationWhenEmptyEmail() {
        validatedObject.setEmail(null);
        when(emailValidator.isValid(null)).thenReturn(false);
        validator.validate(validatedObject, errors);
        assertEquals(1, errors.getErrorCount());
    }

    @Test
    public void shouldNotPassValidationWhenEmptyUsername() {
        validatedObject.setUsername(null);
        validator.validate(validatedObject, errors);
        assertEquals(1, errors.getErrorCount());
    }

    @Test
    public void shouldNotPassValidationWhenEmptyPassword() {
        validatedObject.setPassword(null);
        validator.validate(validatedObject, errors);
        assertEquals(1, errors.getErrorCount());
    }

    @Test
    public void shouldNotPassValidationWhenEmailInvalid() {
        when(emailValidator.isValid(anyString())).thenReturn(false);
        validator.validate(validatedObject, errors);
        assertEquals(1, errors.getErrorCount());
    }

    @Test
    public void shouldNotPassValidationWhenEmailInDb() {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        validator.validate(validatedObject, errors);
        assertEquals(1, errors.getErrorCount());
    }

    @Test
    public void shouldNotPassValidationWhenUsernameInDb() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        validator.validate(validatedObject, errors);
        assertEquals(1, errors.getErrorCount());
    }


}
