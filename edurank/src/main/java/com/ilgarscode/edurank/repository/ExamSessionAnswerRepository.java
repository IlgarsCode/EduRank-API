package com.ilgarscode.edurank.repository;

import com.ilgarscode.edurank.entity.ExamSession;
import com.ilgarscode.edurank.entity.ExamSessionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamSessionAnswerRepository
        extends JpaRepository<ExamSessionAnswer, Long> {

    List<ExamSessionAnswer> findBySession(
            ExamSession session
    );
}