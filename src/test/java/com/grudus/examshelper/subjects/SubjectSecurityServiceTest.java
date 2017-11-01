package com.grudus.examshelper.subjects;

import com.grudus.examshelper.MockitoExtension;
import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.grudus.examshelper.utils.Utils.randomId;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectSecurityServiceTest {

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectSecurityService subjectSecurityService;

    @Test
    void shouldDetectIfBelongsToAnotherUser() throws Exception {
        when(subjectService.belongsToAnotherUser(anyLong(), anyLong())).thenReturn(true);
        AuthenticatedUser user = mock(AuthenticatedUser.class);
        when(user.getUserId()).thenReturn(randomId());

        assertFalse(subjectSecurityService.hasAccessToSubject(user, randomId()));
    }
}