package com.grudus.examshelper.dao;

import com.grudus.examshelper.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectDao extends JpaRepository<Subject, Long> {

    List<Subject> findByUserId(Long userId);

    Optional<Subject> findByUserIdAndId(Long userId, Long id);

    @Transactional
    void deleteByUserIdAndId(Long userId, Long id);
}
