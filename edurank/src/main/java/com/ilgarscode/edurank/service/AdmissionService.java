package com.ilgarscode.edurank.service;

import com.ilgarscode.edurank.entity.Major;
import com.ilgarscode.edurank.entity.Student;
import com.ilgarscode.edurank.repository.MajorRepository;
import com.ilgarscode.edurank.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AdmissionService {

    private final StudentRepository studentRepository;
    private final MajorRepository majorRepository;

    public AdmissionService(
            StudentRepository studentRepository,
            MajorRepository majorRepository
    ) {
        this.studentRepository = studentRepository;
        this.majorRepository = majorRepository;
    }

    public void calculateAdmissions() {

        List<Student> students = studentRepository.findAll();

        students.sort(
                Comparator.comparing(
                        Student::getScore,
                        Comparator.nullsLast(Integer::compareTo)
                ).reversed()
        );

        List<Major> majors = majorRepository.findAll();

        for (Student student : students) {

            if (student.getChoices() == null ||
                    student.getChoices().isEmpty()) {

                student.setStatus("Rədd edildi");
                student.setApproved(false);

                studentRepository.save(student);
                continue;
            }

            boolean admitted = false;

            for (String choice : student.getChoices()) {

                Major major = majors.stream()
                        .filter(m ->
                                m.getName().equalsIgnoreCase(choice))
                        .findFirst()
                        .orElse(null);

                if (major == null) {
                    continue;
                }

                if (major.getQuota() > 0) {

                    major.setQuota(
                            major.getQuota() - 1
                    );

                    majorRepository.save(major);

                    student.setAdmittedMajor(
                            major.getName()
                    );

                    student.setStatus(
                            "Təsdiq gözləyir"
                    );

                    student.setApproved(false);

                    studentRepository.save(student);

                    admitted = true;
                    break;
                }
            }

            if (!admitted) {

                student.setStatus(
                        "Rədd edildi"
                );

                student.setApproved(false);

                studentRepository.save(student);
            }
        }
    }

    public List<Student> getResults() {
        return studentRepository.findAll();
    }

    public List<Student> getPendingResults() {

        return studentRepository.findAll()
                .stream()
                .filter(student ->
                        Boolean.FALSE.equals(
                                student.getApproved()
                        ))
                .toList();
    }

    public Student approveStudent(String studentId) {

        Student student =
                studentRepository.findById(studentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Student tapılmadı"
                                ));

        student.setApproved(true);

        student.setStatus("Qəbul oldu");

        return studentRepository.save(student);
    }

    public Student rejectStudent(
            String studentId
    ) {

        Student student =
                studentRepository.findById(studentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Student tapılmadı"
                                ));

        student.setApproved(false);

        student.setStatus(
                "Rədd edildi"
        );

        return studentRepository.save(student);
    }
}