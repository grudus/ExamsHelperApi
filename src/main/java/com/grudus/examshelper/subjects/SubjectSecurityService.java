package com.grudus.examshelper.subjects;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class SubjectSecurityService {

    public static final String ACCESS_FORMAT = "@subjectSecurityService.hasAccessToSubject(%s, %d)";

    private final SubjectService subjectService;

    @Autowired
    public SubjectSecurityService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public boolean hasAccessToSubject(AuthenticatedUser user, @Nullable Long subjectId) {
        return !subjectService.belongsToAnotherUser(user.getUserId(), subjectId);
    }
}
