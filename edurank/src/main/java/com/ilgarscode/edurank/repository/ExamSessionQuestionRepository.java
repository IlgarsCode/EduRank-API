package com.ilgarscode.edurank.repository;

import com.ilgarscode.edurank.entity.ExamSession;
import com.ilgarscode.edurank.entity.ExamSessionQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamSessionQuestionRepository
        extends JpaRepository<ExamSessionQuestion, Long> {

    List<ExamSessionQuestion> findBySession(
            ExamSession session
    );
}