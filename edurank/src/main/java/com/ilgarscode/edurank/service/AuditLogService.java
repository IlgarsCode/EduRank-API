package com.ilgarscode.edurank.service;

import com.ilgarscode.edurank.entity.AuditLog;
import com.ilgarscode.edurank.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository repository;

    public AuditLogService(
            AuditLogRepository repository
    ) {
        this.repository = repository;
    }
    public List<AuditLog> getAllLogs() {
        return repository.findAll();
    }

    public List<AuditLog> getLogsByStudent(
            String studentId
    ) {
        return repository.findByStudentId(studentId);
    }

    public void log(
            String studentId,
            String action
    ) {

        AuditLog auditLog = new AuditLog();

        auditLog.setStudentId(studentId);
        auditLog.setAction(action);
        auditLog.setCreatedAt(LocalDateTime.now());

        repository.save(auditLog);
    }
}