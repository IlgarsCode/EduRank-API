package com.ilgarscode.edurank.repository;

import com.ilgarscode.edurank.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}