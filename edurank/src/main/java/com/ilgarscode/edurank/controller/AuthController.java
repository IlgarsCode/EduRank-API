package com.ilgarscode.edurank.controller;

import com.ilgarscode.edurank.dto.login.LoginRequestDto;
import com.ilgarscode.edurank.dto.login.LoginResponseDto;
import com.ilgarscode.edurank.entity.Student;
import com.ilgarscode.edurank.repository.StudentRepository;
import com.ilgarscode.edurank.security.JwtService;
import com.ilgarscode.edurank.service.AuditLogService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final StudentRepository studentRepository;
    private final JwtService jwtService;
    private final AuditLogService auditLogService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            StudentRepository studentRepository,
            JwtService jwtService,
            AuditLogService auditLogService,
            PasswordEncoder passwordEncoder
    ) {
        this.studentRepository = studentRepository;
        this.jwtService = jwtService;
        this.auditLogService = auditLogService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public LoginResponseDto login(
            @RequestBody LoginRequestDto request
    ) {

        Student student = studentRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "İstifadəçi tapılmadı"
                        ));

        if (!passwordEncoder.matches(
                request.getPassword(),
                student.getPassword()
        )) {

            throw new RuntimeException(
                    "Şifrə yanlışdır"
            );
        }

        String token =
                jwtService.generateToken(
                        student.getId(),
                        student.getRole()
                );

        auditLogService.log(
                student.getId(),
                "LOGIN_SUCCESS"
        );

        return new LoginResponseDto(token);
    }
}