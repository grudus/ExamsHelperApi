package com.grudus.examshelper.subjects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    Optional<SubjectDto> findByLabel(Long userId, String label) {
        return subjectDao.findByUserIdAndLabel(userId, label);
    }
}
