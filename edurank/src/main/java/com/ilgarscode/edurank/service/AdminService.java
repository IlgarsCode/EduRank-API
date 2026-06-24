package com.ilgarscode.edurank.service;

import com.ilgarscode.edurank.dto.admin.AdminCreateDto;
import com.ilgarscode.edurank.entity.Student;
import com.ilgarscode.edurank.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final StudentRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(
            StudentRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Student createAdmin(
            AdminCreateDto dto
    ) {

        if (repository.existsById(dto.getId())) {

            throw new RuntimeException(
                    "Bu FIN artıq mövcuddur"
            );
        }

        if (repository.existsByEmail(dto.getEmail())) {

            throw new RuntimeException(
                    "Bu email artıq istifadə olunur"
            );
        }

        Student admin = new Student();

        admin.setId(dto.getId());
        admin.setName(dto.getName());
        admin.setSurname(dto.getSurname());
        admin.setEmail(dto.getEmail());

        admin.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()
                )
        );

        admin.setRole("ADMIN");

        admin.setApproved(true);

        admin.setStatus("ACTIVE");

        return repository.save(admin);
    }
}