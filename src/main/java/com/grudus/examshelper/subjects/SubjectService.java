package com.grudus.examshelper.subjects;

import com.grudus.examshelper.users.User;
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

    public void deleteAll(User user) {
        subjectDao.deleteByUser(user.getId());
    }

    public void save(Subject subject) {
        subjectDao.save(subject);
    }

    public void update(Subject subject) {
        subjectDao.update(subject);
    }

    public void deleteByUserIdAndAndoridId(Long userId, Long id) {
        subjectDao.deleteByUserIdAndAndroidId(userId, id);
    }

    public List<Subject> findByUser(Long id) {
        return subjectDao.findByUserId(id);
    }

    public Optional<Subject> findByUserAndAndroidId(Long userId, Long id) {
        return subjectDao.findByUserIdAndAndroidId(userId, id);
    }
}
