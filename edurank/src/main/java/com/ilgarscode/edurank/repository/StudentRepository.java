package com.ilgarscode.edurank.repository;

import com.ilgarscode.edurank.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsById(String id);

    List<Student> findAllByOrderByScoreDesc();
}