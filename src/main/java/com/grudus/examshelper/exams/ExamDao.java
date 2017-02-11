package com.grudus.examshelper.exams;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamDao extends JpaRepository<Exam, Long>{
}
