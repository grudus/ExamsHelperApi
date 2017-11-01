package com.grudus.examshelper.stats;

import com.grudus.examshelper.configuration.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class SubjectStatsController {

    private final StatsService statsService;

    @Autowired
    public SubjectStatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/average")
    @PreAuthorize("@subjectSecurityService.hasAccessToSubject(#user, #subjectId)")
    public List<AverageExamsGradePerMonth> getAverageExamsGradePerMonth(AuthenticatedUser user,
                                                                        @RequestParam(required = false) Long subjectId) {
        return statsService.getAverageExamsGradePerMonth(user.getUser(), subjectId);
    }

}
