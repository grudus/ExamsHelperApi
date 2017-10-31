package com.grudus.examshelper.subjects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@Service
public class SubjectService {

    private final SubjectDao subjectDao;

    @Autowired
    public SubjectService(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    public boolean exists(Long id) {
        requireNonNull(id);
        return subjectDao.exists(id);
    }

    public Long save(Subject subject) {
        requireNonNull(subject);
        return subjectDao.save(subject);
    }

    boolean belongsToAnotherUser(Long userId, @Nullable Long subjectId) {
        requireNonNull(userId);

        return ofNullable(subjectId)
                .flatMap(id -> subjectDao.findById(subjectId))
                .filter(subject -> !Objects.equals(subject.getUserId(), userId))
                .isPresent();
    }

    void update(Subject subject) {
        requireNonNull(subject);
        subjectDao.update(subject);
    }

    List<Subject> findByUser(Long id) {
        requireNonNull(id);
        return subjectDao.findByUserId(id);
    }

    boolean labelExists(String label, Long userId) {
        requireNonNull(label);
        requireNonNull(userId);
        return subjectDao.labelExists(label, userId);
    }

    void delete(Long id) {
        requireNonNull(id);
        subjectDao.delete(id);
    }

    Optional<SubjectDto> findById(Long id) {
        requireNonNull(id);
        return subjectDao.findById(id).map(Subject::toDto);
    }
}
