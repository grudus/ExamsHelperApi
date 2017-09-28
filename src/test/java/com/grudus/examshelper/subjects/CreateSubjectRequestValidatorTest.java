package com.grudus.examshelper.subjects;

import com.grudus.examshelper.MockitoExtension;
import com.grudus.examshelper.commons.keys.RestKeys;
import com.grudus.examshelper.users.auth.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static com.grudus.examshelper.utils.Utils.*;
import static com.grudus.examshelper.utils.ValidatorUtils.assertErrorKeys;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateSubjectRequestValidatorTest {

    private final int INVALID_LENGTH = Subject.MAX_LABEL_LENGTH * 2;
    private final int VALID_LENGTH = Subject.MAX_LABEL_LENGTH / 2;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private CreateSubjectRequestValidator validator;

    private Errors errors;
    private SubjectDto subject;

    @BeforeEach
    public void init() {
        errors = new BeanPropertyBindingResult(subject, "subjectDto");
        when(authenticationService.getCurrentLoggedUserId()).thenReturn(randomId());
        when(subjectService.labelExists(anyString(), anyLong())).thenReturn(false);
    }

    @Test
    public void shouldValidateProperly() {
        subject = new SubjectDto(randomId(), randAlph(VALID_LENGTH), randomColor());

        validator.validate(subject, errors);

        assertEquals(0, errors.getErrorCount());
    }

    @Test
    public void shouldNotPassValidationWhenNoLabel() {
        subject = new SubjectDto(randomId(), null, randomColor());

        validator.validate(subject, errors);

        assertErrorKeys(errors, RestKeys.INVALID_LABEL);
    }

    @Test
    public void shouldNotPassValidationWhenEmptyLabel() {
        subject = new SubjectDto(randomId(), " \t ", randomColor());

        validator.validate(subject, errors);

        assertErrorKeys(errors, RestKeys.INVALID_LABEL);
    }

    @Test
    public void shouldNotPassValidationWhenLabelTooLong() {
        subject = new SubjectDto(randomId(), randAlph(INVALID_LENGTH), randomColor());

        validator.validate(subject, errors);

        assertErrorKeys(errors, RestKeys.INVALID_LABEL);
    }

    @Test
    public void shouldNotPassValidationWhenLabelExists() {
        subject = new SubjectDto(randomId(), randAlph(VALID_LENGTH), randomColor());
        when(subjectService.labelExists(anyString(), anyLong())).thenReturn(true);

        validator.validate(subject, errors);

        assertErrorKeys(errors, RestKeys.LABEL_EXISTS);
    }

    @Test
    public void shouldNotPassValidationWhenNoColor() {
        subject = new SubjectDto(randomId(), randAlph(VALID_LENGTH), null);

        validator.validate(subject, errors);

        assertErrorKeys(errors, RestKeys.INVALID_COLOR);
    }

    @Test
    public void shouldNotPassValidationWhenInvalidColor() {
        subject = new SubjectDto(randomId(), randAlph(VALID_LENGTH), "#xyzabc");

        validator.validate(subject, errors);

        assertErrorKeys(errors, RestKeys.INVALID_COLOR);
    }

    @Test
    public void shouldNotPassValidationWhenColorTooLong() {
        subject = new SubjectDto(randomId(), randAlph(VALID_LENGTH), "#abcdef12");

        validator.validate(subject, errors);

        assertErrorKeys(errors, RestKeys.INVALID_COLOR);
    }

    @Test
    public void shouldNotPassValidationWhenColorWithoutHashtag() {
        subject = new SubjectDto(randomId(), randAlph(VALID_LENGTH), "123456");

        validator.validate(subject, errors);

        assertErrorKeys(errors, RestKeys.INVALID_COLOR);
    }
}