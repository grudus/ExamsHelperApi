package com.grudus.examshelper.subjects;

import com.grudus.examshelper.tables.Subjects;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SubjectDao {

    public static final Subjects S = Subjects.SUBJECTS;

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

    Optional<Subject> findByUserIdAndAndroidId(Long userId, Long androidId) {
        return dsl.selectFrom(S)
                .where(S.USER_ID.eq(userId).and(S.ANDROID_ID.eq(androidId)))
                .fetchOptionalInto(Subject.class);
    }
    void deleteByUserIdAndAndroidId(Long userId, Long androidId) {
        dsl.deleteFrom(S)
                .where(S.USER_ID.eq(userId).and(S.ANDROID_ID.eq(androidId)))
                .execute();
    }

    void deleteByUser(Long userId) {
        dsl.deleteFrom(S)
                .where(S.USER_ID.eq(userId))
                .execute();
    }

    void save(Subject subject) {
        dsl.newRecord(S, subject).insert();
    }

    void update(Subject subject) {
        dsl.newRecord(S, subject).update();
    }
}
