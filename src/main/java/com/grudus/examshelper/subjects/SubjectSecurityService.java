package com.grudus.examshelper.subjects;

import com.grudus.examshelper.exceptions.IllegalActionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class SubjectSecurityService {

    private final SubjectService subjectService;

    @Autowired
    public SubjectSecurityService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void assertSubjectBelongsToUser(Long userId, @Nullable Long subjectId) {
        if (subjectService.belongsToAnotherUser(userId, subjectId))
            throw new IllegalActionException("User {%d} tries to steal someone else's subject {%d}!", userId, subjectId);
    }
}
