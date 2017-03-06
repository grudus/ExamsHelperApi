package com.grudus.examshelper.subjects;

import com.grudus.examshelper.tables.Subjects;
import com.grudus.examshelper.tables.records.SubjectsRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Repository
public class SubjectDao {

    private static final Subjects S = Subjects.SUBJECTS;

    private final DSLContext dsl;

    @Autowired
    public SubjectDao(DSLContext dsl) {
        this.dsl = dsl;
    }

    List<Subject> findByUserId(Long userId) {
        return dsl.selectFrom(S)
                .where(S.USER_ID.eq(userId))
                .fetchInto(Subject.class);
    }

    Optional<Subject> findById(Long id) {
        return dsl.selectFrom(S)
                .where(S.ID.eq(id))
                .fetchOptionalInto(Subject.class);
    }

    void save(Subject subject) {
        SubjectsRecord record = dsl.newRecord(S, subject);
        record.insert();
        subject.setId(record.getId());
    }

    void update(Subject subject) {
        subject.setLastModified(now());
        dsl.newRecord(S, subject).update();
    }

    public void delete(Long id) {
        dsl.deleteFrom(S)
                .where(S.ID.eq(id))
                .execute();
    }
}
