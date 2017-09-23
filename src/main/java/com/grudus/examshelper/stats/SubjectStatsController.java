package com.grudus.examshelper.stats;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import com.grudus.examshelper.subjects.SubjectSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class SubjectStatsController {

    private final StatsService statsService;
    private final SubjectSecurityService subjectSecurityService;

    @Autowired
    public SubjectStatsController(StatsService statsService, SubjectSecurityService subjectSecurityService) {
        this.statsService = statsService;
        this.subjectSecurityService = subjectSecurityService;
    }

    @GetMapping("/average")
    public List<AverageExamsGradePerMonth> getAverageExamsGradePerMonth(AuthenticatedUser user,
                                                                        @RequestParam(required = false) Long subjectId) {
        subjectSecurityService.assertSubjectBelongsToUser(user.getUserId(), subjectId);
        return statsService.getAverageExamsGradePerMonth(user.getUser(), subjectId);
    }

}
