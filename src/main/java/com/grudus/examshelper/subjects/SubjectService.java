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

    public void deleteAll(List<Subject> subjects) {
        subjectDao.delete(subjects);
    }

    public void save(Subject subject) {
        subjectDao.saveAndFlush(subject);
    }

    public void update(Subject subject) {
        subjectDao.save(subject);
    }

    public void deleteByUserIdAndId(Long userId, Long id) {
        subjectDao.deleteByUserIdAndId(userId, id);
    }

    public List<Subject> findByUser(Long id) {
        return subjectDao.findByUserId(id);
    }

    public Optional<Subject> findByUserAndId(Long userId, Long id) {
        return subjectDao.findByUserIdAndId(userId, id);
    }
}
