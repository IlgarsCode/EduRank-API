package com.ilgarscode.edurank.controller;

import com.ilgarscode.edurank.dto.student.StudentRegisterDto;
import com.ilgarscode.edurank.entity.Student;
import com.ilgarscode.edurank.service.StudentService;
import com.ilgarscode.edurank.dto.student.StudentChoicesDto;
import com.ilgarscode.edurank.dto.student.StudentResultDto;
import com.ilgarscode.edurank.dto.student.StudentRankingDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public Student register(@RequestBody StudentRegisterDto dto) {

        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        student.setSurname(dto.getSurname());
        student.setEmail(dto.getEmail());
        student.setPassword(dto.getPassword());

        return service.register(student);
    }

    @GetMapping
    public List<Student> getAll() {
        return service.getAll();
    }

    @PostMapping("/{studentId}/choices")
    public Student saveChoices(
            @PathVariable String studentId,
            @RequestBody StudentChoicesDto dto
    ) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String currentUserId =
                authentication.getName();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority()
                                        .equals("ROLE_ADMIN"));

        if (!isAdmin &&
                !currentUserId.equals(studentId)) {

            throw new RuntimeException(
                    "Başqa tələbənin seçimlərini dəyişə bilməzsiniz"
            );
        }

        return service.saveChoices(
                studentId,
                dto.getChoices()
        );
    }

    @GetMapping("/{studentId}/result")
    public StudentResultDto getResult(
            @PathVariable String studentId
    ) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String currentUserId =
                authentication.getName();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority()
                                        .equals("ROLE_ADMIN"));

        if (!isAdmin &&
                !currentUserId.equals(studentId)) {

            throw new RuntimeException(
                    "Başqa tələbənin nəticəsinə baxa bilməzsiniz"
            );
        }

        return service.getResult(studentId);
    }

    @GetMapping("/ranking")
    public List<StudentRankingDto> getRanking() {
        return service.getRanking();
    }
    @GetMapping("/my-result")
    public StudentResultDto myResult() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String studentId =
                authentication.getName();

        return service.getResult(
                studentId
        );
    }
}