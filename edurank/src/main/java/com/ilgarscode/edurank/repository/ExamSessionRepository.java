package com.ilgarscode.edurank.repository;

import com.ilgarscode.edurank.entity.ExamSession;
import com.ilgarscode.edurank.entity.ExamStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamSessionRepository
        extends JpaRepository<ExamSession, Long> {

    Optional<ExamSession> findByIdAndStudent_Id(
            Long id,
            String studentId
    );

    List<ExamSession> findByStudent_IdOrderByIdDesc(
            String studentId
    );

    List<ExamSession> findByStatus(
            ExamStatus status
    );

    Optional<ExamSession> findTopByStudent_IdOrderByIdDesc(
            String studentId
    );

    List<ExamSession> findAllByOrderByIdDesc();
}