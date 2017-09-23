package com.grudus.examshelper.subjects;

import com.grudus.examshelper.exceptions.IllegalActionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.grudus.examshelper.utils.Utils.randomId;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubjectSecurityServiceTest {

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectSecurityService subjectSecurityService;

    @Test(expected = IllegalActionException.class)
    public void shouldThrowExceptionWhenSubjectDoNotBelongsToUser() throws Exception {
        when (subjectService.belongsToUser(anyLong(), anyLong())).thenReturn(false);

        subjectSecurityService.assertSubjectBelongsToUser(randomId(), randomId());
    }

}