package com.grudus.examshelper.exams;

import com.grudus.examshelper.tables.Exams;
import com.grudus.examshelper.tables.Subjects;
import com.grudus.examshelper.tables.records.ExamsRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExamDao {

    private static final Exams E = Exams.EXAMS;
    private static final Subjects S = Subjects.SUBJECTS;

    private final DSLContext dsl;

    @Autowired
    public ExamDao(DSLContext dsl) {
        this.dsl = dsl;
    }

    List<ExamDto> findAllAsExamDtoByUserId(Long userId) {
        return dsl.select(E.ID, E.INFO, E.DATE, E.GRADE, S.ID, S.LABEL.as("subject.label"), S.COLOR.as("subject.color"))
                .from(E).innerJoin(S).onKey()
                .where(S.USER_ID.eq(userId))
                .fetchInto(ExamDto.class);
    }

    void save(Exam exam) {
        ExamsRecord record = dsl.newRecord(E, exam);
        record.insert();
        exam.setId(record.getId());
    }

}
