package com.ilgarscode.edurank.service;

import com.ilgarscode.edurank.dto.student.StudentResultDto;
import com.ilgarscode.edurank.entity.Student;
import com.ilgarscode.edurank.repository.StudentRepository;
import com.ilgarscode.edurank.dto.student.StudentRankingDto;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository repo;
    private final PasswordEncoder passwordEncoder;

    public StudentService(
            StudentRepository repo,
            PasswordEncoder passwordEncoder
    ) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public Student register(Student student) {

        if (repo.existsByEmail(student.getEmail())) {

            throw new RuntimeException(
                    "Bu email artıq istifadə olunur"
            );
        }

        if (repo.existsById(student.getId())) {

            throw new RuntimeException(
                    "Bu FIN artıq qeydiyyatdan keçib"
            );
        }

        student.setPassword(
                passwordEncoder.encode(
                        student.getPassword()
                )
        );

        student.setRole("STUDENT");
        student.setStatus("Gözləyir");
        student.setScore(null);
        student.setApproved(false);

        return repo.save(student);
    }

    public Student login(String email, String password) {

        Student student = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        if (!passwordEncoder.matches(
                password,
                student.getPassword()
        )) {

            throw new RuntimeException(
                    "Şifrə yanlışdır"
            );
        }

        return student;
    }

    public void updateStudentScore(String studentId, Integer score) {
        Student student = repo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student tapılmadı"));

        student.setScore(score);
        repo.save(student);
    }

    public List<Student> getAll() {
        return repo.findAll();
    }

    public Student saveChoices(
            String studentId,
            List<String> choices
    ) {

        Student student = repo.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student tapılmadı"));

        student.setChoices(choices);

        return repo.save(student);
    }

    public Student getById(String studentId) {

        return repo.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student tapılmadı"));
    }
    public StudentResultDto getResult(String studentId) {

        Student student = repo.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student tapılmadı"));

        return new StudentResultDto(
                student.getId(),
                student.getName() + " " + student.getSurname(),
                student.getScore(),
                student.getAdmittedMajor(),
                student.getStatus()
        );
    }
    public List<StudentRankingDto> getRanking() {

        List<Student> students =
                repo.findAllByOrderByScoreDesc();

        List<StudentRankingDto> ranking =
                new ArrayList<>();

        int rank = 1;

        for (Student student : students) {

            if (student.getScore() == null) {
                continue;
            }

            ranking.add(
                    new StudentRankingDto(
                            rank++,
                            student.getId(),
                            student.getName() + " " + student.getSurname(),
                            student.getScore()
                    )
            );
        }

        return ranking;
    }
}