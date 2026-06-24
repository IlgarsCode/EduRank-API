package com.ilgarscode.edurank.controller;

import com.ilgarscode.edurank.entity.AuditLog;
import com.ilgarscode.edurank.service.AuditLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/logs")
public class AuditLogController {

    private final AuditLogService service;

    public AuditLogController(
            AuditLogService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<AuditLog> getAllLogs() {

        return service.getAllLogs();
    }

    @GetMapping("/{studentId}")
    public List<AuditLog> getStudentLogs(
            @PathVariable String studentId
    ) {

        return service.getLogsByStudent(
                studentId
        );
    }
}