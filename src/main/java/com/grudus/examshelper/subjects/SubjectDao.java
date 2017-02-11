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
        dsl.insertInto(S)
                .set(S.ANDROID_ID, subject.getAndroidId())
                .set(S.USER_ID, subject.getUser().getId())
                .set(S.LABEL, subject.getLabel())
                .set(S.COLOR, subject.getColor())
                .set(S.LAST_MODIFIED, subject.getLastModified())
                .execute();
    }

    void update(Subject subject) {
        dsl.update(S)
                .set(S.ANDROID_ID, subject.getAndroidId())
                .set(S.USER_ID, subject.getUser().getId())
                .set(S.LABEL, subject.getLabel())
                .set(S.COLOR, subject.getColor())
                .set(S.LAST_MODIFIED, subject.getLastModified())
                .where(S.ID.eq(subject.getId()))
                .execute();
    }
}
