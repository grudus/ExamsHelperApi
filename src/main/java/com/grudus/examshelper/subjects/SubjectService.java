package com.grudus.examshelper.subjects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SubjectService {

    private final SubjectDao subjectDao;

    @Autowired
    public SubjectService(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    public Long save(Subject subject) {
        return subjectDao.save(subject);
    }

    boolean belongsToAnotherUser(Long userId, Long subjectId) {
        return subjectDao.findById(subjectId)
                .filter(subject -> !Objects.equals(subject.getUserId(), userId))
                .isPresent();
    }

    void update(Subject subject) {
        subjectDao.update(subject);
    }

    List<Subject> findByUser(Long id) {
        return subjectDao.findByUserId(id);
    }

    public boolean exists(Long id) {
        return subjectDao.exists(id);
    }

    boolean labelExists(String label, Long userId) {
        return subjectDao.labelExists(label, userId);
    }

    void delete(Long id) {
        subjectDao.delete(id);
    }

    Optional<SubjectDto> findById(Long id) {
        return subjectDao.findById(id).map(Subject::toDto);
    }
}
