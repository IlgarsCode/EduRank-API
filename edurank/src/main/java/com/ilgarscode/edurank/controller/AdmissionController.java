package com.ilgarscode.edurank.controller;

import com.ilgarscode.edurank.entity.Student;
import com.ilgarscode.edurank.service.AdmissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admission")
public class AdmissionController {

    private final AdmissionService service;

    public AdmissionController(
            AdmissionService service
    ) {
        this.service = service;
    }

    @GetMapping("/pending")
    public List<Student> pending() {

        return service.getPendingResults();
    }

    @PostMapping("/approve/{studentId}")
    public Student approve(
            @PathVariable String studentId
    ) {

        return service.approveStudent(
                studentId
        );
    }

    @PostMapping("/reject/{studentId}")
    public Student reject(
            @PathVariable String studentId
    ) {

        return service.rejectStudent(
                studentId
        );
    }

    @PostMapping("/calculate")
    public String calculate() {

        service.calculateAdmissions();

        return "Qəbul nəticələri hesablandı";
    }

    @GetMapping("/results")
    public List<Student> results() {
        return service.getResults();
    }
}