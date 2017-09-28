package com.grudus.examshelper.subjects;

import com.grudus.examshelper.MockitoExtension;
import com.grudus.examshelper.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.grudus.examshelper.utils.Utils.randomId;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubjectSecurityServiceTest {

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectSecurityService subjectSecurityService;

    @Test
    public void shouldThrowExceptionWhenSubjectDoNotBelongsToUser() throws Exception {
        when(subjectService.belongsToUser(anyLong(), anyLong())).thenReturn(false);

        assertThrows(IllegalActionException.class, () ->
                subjectSecurityService.assertSubjectBelongsToUser(randomId(), randomId()));
    }

}