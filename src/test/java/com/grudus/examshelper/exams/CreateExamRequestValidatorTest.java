package com.grudus.examshelper.exams;

import com.grudus.examshelper.commons.keys.RestKeys;
import com.grudus.examshelper.subjects.SubjectService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Random;

import static com.grudus.examshelper.utils.Utils.randAlph;
import static com.grudus.examshelper.utils.ValidatorUtils.assertErrorKeys;
import static java.time.LocalDateTime.now;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateExamRequestValidatorTest {

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private CreateExamRequestValidator validator;

    private CreateExamRequest request;
    private Errors errors;

    @Before
    public void init() {
        errors = new BeanPropertyBindingResult(request, "createExamRequest");
        when(subjectService.exists(anyLong())).thenReturn(true);
    }

    @Test
    public void shouldPassValidation() {
        request = randomRequest();
        validator.validate(request, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void shouldPassValidationWhenNoInfo() {
        request = new CreateExamRequest(null, 1L, now());

        validator.validate(request, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void shouldNotPassWhenNoSubjectId() {
        request = new CreateExamRequest(randAlph(11), null, now());

        validator.validate(request, errors);

        assertErrorKeys(errors, RestKeys.INVALID_SUBJECT);
    }

    @Test
    public void shouldNotPassWhenSubjectDoNotExists() {
        when(subjectService.exists(anyLong())).thenReturn(false);

        request = new CreateExamRequest(randAlph(11), 3L, now());

        validator.validate(request, errors);

        assertErrorKeys(errors, RestKeys.INVALID_SUBJECT);
    }

    @Test
    public void shouldNotPassWhenNoDate() {
        request = new CreateExamRequest(randAlph(11), 1L, null);

        validator.validate(request, errors);

        assertErrorKeys(errors, RestKeys.EMPTY_DATE);
    }


    private CreateExamRequest randomRequest() {
        return new CreateExamRequest(randAlph(11), new Random().nextLong(), now());
    }

}