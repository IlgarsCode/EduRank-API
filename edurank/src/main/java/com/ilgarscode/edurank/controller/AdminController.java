package com.ilgarscode.edurank.controller;

import com.ilgarscode.edurank.dto.admin.AdminCreateDto;
import com.ilgarscode.edurank.entity.ExamSession;
import com.ilgarscode.edurank.entity.Student;
import com.ilgarscode.edurank.repository.ExamSessionRepository;
import com.ilgarscode.edurank.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final ExamSessionRepository examSessionRepository;

    public AdminController(
            AdminService adminService,
            ExamSessionRepository examSessionRepository
    ) {
        this.adminService = adminService;
        this.examSessionRepository = examSessionRepository;
    }

    @PostMapping("/create")
    public Student createAdmin(
            @RequestBody AdminCreateDto dto
    ) {
        return adminService.createAdmin(dto);
    }

    @GetMapping("/exam-results")
    public List<ExamSession> getAllExamResults() {

        return examSessionRepository
                .findAllByOrderByIdDesc();
    }

    @GetMapping("/exam-results/{studentId}")
    public List<ExamSession> getStudentResults(
            @PathVariable String studentId
    ) {

        return examSessionRepository
                .findByStudent_IdOrderByIdDesc(
                        studentId
                );
    }
}