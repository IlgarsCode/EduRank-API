package com.ilgarscode.edurank.service;

import com.ilgarscode.edurank.dto.stats.StatsDto;
import com.ilgarscode.edurank.entity.Student;
import com.ilgarscode.edurank.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsService {

    private final StudentRepository repository;

    public StatsService(StudentRepository repository) {
        this.repository = repository;
    }

    public StatsDto getStats() {

        List<Student> students =
                repository.findAll();

        long totalStudents =
                students.size();

        long examParticipants =
                students.stream()
                        .filter(s -> s.getScore() != null)
                        .count();

        long admittedStudents =
                students.stream()
                        .filter(s ->
                                "Qəbul oldu"
                                        .equalsIgnoreCase(
                                                s.getStatus()))
                        .count();

        double averageScore =
                students.stream()
                        .filter(s -> s.getScore() != null)
                        .mapToInt(Student::getScore)
                        .average()
                        .orElse(0);

        int highestScore =
                students.stream()
                        .filter(s -> s.getScore() != null)
                        .mapToInt(Student::getScore)
                        .max()
                        .orElse(0);

        int lowestScore =
                students.stream()
                        .filter(s -> s.getScore() != null)
                        .mapToInt(Student::getScore)
                        .min()
                        .orElse(0);

        return new StatsDto(
                totalStudents,
                examParticipants,
                admittedStudents,
                averageScore,
                highestScore,
                lowestScore
        );
    }
}