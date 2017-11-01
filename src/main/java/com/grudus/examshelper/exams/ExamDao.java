package com.grudus.examshelper.exams;

import com.grudus.examshelper.exams.domain.Exam;
import com.grudus.examshelper.exams.domain.ExamDto;
import com.grudus.examshelper.tables.Exams;
import com.grudus.examshelper.tables.Subjects;
import com.grudus.examshelper.tables.records.ExamsRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record7;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;

@Repository
class ExamDao {

    private static final Exams E = Exams.EXAMS;
    private static final Subjects S = Subjects.SUBJECTS;

    private final DSLContext dsl;

    @Autowired
    ExamDao(DSLContext dsl) {
        this.dsl = dsl;
    }

    List<ExamDto> findAllAsExams(Long userId) {
        return selectExamsAsDto()
                .where(S.USER_ID.eq(userId))
                .fetchInto(ExamDto.class);
    }

    Long save(Exam exam) {
        ExamsRecord record = dsl.newRecord(E, exam);
        record.insert();
        return record.getId();
    }

    List<ExamDto> findAllFromDate(Long id, LocalDateTime dateFrom) {
        return selectExamsAsDto()
                .where(S.USER_ID.eq(id).and(E.DATE.greaterOrEqual(dateFrom)))
                .fetchInto(ExamDto.class);
    }

    Integer countNotGradedFromPast(Long userId) {
        return dsl.fetchCount(E.join(S).onKey(),
                S.USER_ID.eq(userId)
                        .and(E.DATE.lessThan(now()))
                        .and(withoutGradeCondition()));
    }

    List<ExamDto> findWithoutGradeForSubject(Long subjectId) {
        return selectExamsAsDto()
                .where((E.SUBJECT_ID.eq(subjectId))
                        .and(E.DATE.lt(now()))
                        .and(withoutGradeCondition()))
                .fetchInto(ExamDto.class);
    }

    List<ExamDto> findWithoutGrade() {
        return selectExamsAsDto()
                .where(E.DATE.lt(now()))
                .and(withoutGradeCondition())
                .fetchInto(ExamDto.class);
    }


    boolean belongsToAnotherUser(Long userId, Long examId) {
        return dsl.fetchExists(E.join(S).onKey(), E.ID.eq(examId).and(S.USER_ID.notEqual(userId)));
    }

    void updateGrade(Long examId, Double grade) {
        dsl.update(E)
                .set(E.LAST_MODIFIED, now())
                .set(E.GRADE, grade)
                .where(E.ID.eq(examId))
                .execute();
    }

    void delete(Long examId) {
        dsl.deleteFrom(E)
                .where(E.ID.eq(examId))
                .execute();
    }

    private Condition withoutGradeCondition() {
        return E.GRADE.isNull().or(E.GRADE.le(0D));
    }

    private SelectJoinStep<Record7<Long, String, LocalDateTime, Double, Long, String, String>> selectExamsAsDto() {
        return dsl.select(E.ID, E.INFO, E.DATE, E.GRADE,
                S.ID.as("subject.id"), S.LABEL.as("subject.label"), S.COLOR.as("subject.color"))
                .from(E).innerJoin(S).onKey();
    }
}
