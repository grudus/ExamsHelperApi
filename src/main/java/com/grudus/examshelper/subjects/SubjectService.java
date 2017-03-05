package com.grudus.examshelper.subjects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectDao subjectDao;

    @Autowired
    public SubjectService(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    void save(Subject subject) {
        subjectDao.save(subject);
    }

    void update(Subject subject) {
        subjectDao.update(subject);
    }

    List<Subject> findByUser(Long id) {
        return subjectDao.findByUserId(id);
    }

}
