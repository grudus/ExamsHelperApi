package com.grudus.examshelper.subjects;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SubjectDao {

    List<Subject> findByUserId(Long userId) {return null;}
    Optional<Subject> findByUserIdAndId(Long userId, Long id) {return null;}
    void deleteByUserIdAndId(Long userId, Long id) {return;}
}
