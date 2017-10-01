package com.grudus.examshelper.users.auth;

import com.grudus.examshelper.MockitoExtension;
import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.grudus.examshelper.commons.keys.RestKeys.*;
import static com.grudus.examshelper.utils.Utils.randAlph;
import static com.grudus.examshelper.utils.Utils.randomEmail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddUserRequestValidatorTest {

    @Mock
    private EmailValidator emailValidator;

    @Mock
    private UserService userService;

    private AddUserRequestValidator validator;

    private AddUserRequest validatedObject;
    private Errors errors;

    @BeforeEach
    void init() {
        validatedObject = new AddUserRequest(randAlph(10), randAlph(10), randomEmail());
        errors = new BeanPropertyBindingResult(validatedObject, "addUserRequest");
        validator = new AddUserRequestValidator(userService, emailValidator);

        when(emailValidator.isValid(anyString())).thenReturn(true);
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
    }

    @Test
    void shouldValidateProperly() {
        validator.validate(validatedObject, errors);
        assertEquals(0, errors.getErrorCount());
    }

    @Test
    void shouldNotPassValidationWhenEmptyEmail() {
        validatedObject.setEmail(null);

        validator.validate(validatedObject, errors);

        assertEquals(1, errors.getErrorCount());
        assertThat(codes(), hasItem(EMPTY_EMAIL));
    }

    @Test
    void shouldNotPassValidationWhenInvalidEmail() {
        when(emailValidator.isValid(anyString())).thenReturn(false);

        validator.validate(validatedObject, errors);

        assertEquals(1, errors.getErrorCount());
        assertThat(codes(), hasItem(INVALID_EMAIL));
    }

    @Test
    void shouldNotPassValidationWhenEmptyUsername() {
        validatedObject.setUsername(null);

        validator.validate(validatedObject, errors);

        assertEquals(1, errors.getErrorCount());
        assertThat(codes(), hasItem(EMPTY_USERNAME));
    }

    @Test
    void shouldNotPassValidationWhenEmptyPassword() {
        validatedObject.setPassword(null);

        validator.validate(validatedObject, errors);

        assertEquals(1, errors.getErrorCount());
        assertThat(codes(), hasItem(EMPTY_PASSWORD));
    }


    @Test
    void shouldNotPassValidationWhenPasswordTooShort() {
        validatedObject.setPassword(randAlph(5));

        validator.validate(validatedObject, errors);

        assertEquals(1, errors.getErrorCount());
        assertThat(codes(), hasItem(TOO_SHORT_PASSWORD));
    }

    @Test
    void shouldNotPassValidationWhenEmailInvalid() {
        when(emailValidator.isValid(anyString())).thenReturn(false);

        validator.validate(validatedObject, errors);

        assertEquals(1, errors.getErrorCount());
        assertThat(codes(), hasItem(INVALID_EMAIL));
    }

    @Test
    void shouldNotPassValidationWhenEmailInDb() {
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        validator.validate(validatedObject, errors);

        assertEquals(1, errors.getErrorCount());
        assertThat(codes(), hasItem(EMAIL_EXISTS));
    }

    @Test
    void shouldNotPassValidationWhenUsernameInDb() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        validator.validate(validatedObject, errors);

        assertEquals(1, errors.getErrorCount());
        assertThat(codes(), hasItem(USERNAME_EXISTS));
    }

    @Test
    void shouldHasMultipleErrors() {
        validatedObject.setPassword("   ");
        validatedObject.setEmail("  ");
        validatedObject.setUsername(" ");

        validator.validate(validatedObject, errors);

        assertEquals(3, errors.getErrorCount());
        assertThat(codes(), containsInAnyOrder(EMPTY_EMAIL, EMPTY_PASSWORD, EMPTY_USERNAME));
    }

    private List<String> codes() {
        return errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getCode).collect(Collectors.toList());
    }


}
