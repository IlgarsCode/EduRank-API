package com.ilgarscode.edurank.repository;

import com.ilgarscode.edurank.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository
        extends JpaRepository<Major, Long> {
}