package com.ilgarscode.edurank.repository;

import com.ilgarscode.edurank.entity.ExamConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamConfigRepository
        extends JpaRepository<ExamConfig, Long> {
}