package com.ilgarscode.edurank.repository;

import com.ilgarscode.edurank.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByStudentId(
            String studentId
    );
}