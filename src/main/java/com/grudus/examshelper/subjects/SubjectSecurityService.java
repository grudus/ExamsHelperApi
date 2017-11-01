package com.grudus.examshelper.subjects;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
public class SubjectSecurityService {

    private final SubjectService subjectService;

    @Autowired
    public SubjectSecurityService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public boolean hasAccessToSubject(AuthenticatedUser user, @Nullable Long subjectId) {
        requireNonNull(user);
        return !subjectService.belongsToAnotherUser(user.getUserId(), subjectId);
    }
}
