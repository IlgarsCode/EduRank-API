package com.ilgarscode.edurank.service;

import com.ilgarscode.edurank.entity.ExamSession;
import com.ilgarscode.edurank.entity.ExamSessionAnswer;
import com.ilgarscode.edurank.entity.ExamStatus;
import com.ilgarscode.edurank.repository.ExamSessionAnswerRepository;
import com.ilgarscode.edurank.repository.ExamSessionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamSchedulerService {

    private final ExamSessionRepository examSessionRepository;
    private final ExamSessionAnswerRepository answerRepository;
    private final StudentService studentService;
    private final AuditLogService auditLogService;

    public ExamSchedulerService(
            ExamSessionRepository examSessionRepository,
            ExamSessionAnswerRepository answerRepository,
            StudentService studentService,
            AuditLogService auditLogService
    ) {
        this.examSessionRepository = examSessionRepository;
        this.answerRepository = answerRepository;
        this.studentService = studentService;
        this.auditLogService = auditLogService;
    }

    @Scheduled(fixedRate = 30000)
    public void finishExpiredExams() {

        List<ExamSession> activeSessions =
                examSessionRepository.findByStatus(
                        ExamStatus.ACTIVE
                );

        for (ExamSession session : activeSessions) {

            if (LocalDateTime.now().isAfter(
                    session.getEndsAt()
            )) {

                int score = 0;

                List<ExamSessionAnswer> answers =
                        answerRepository.findBySession(
                                session
                        );

                for (ExamSessionAnswer answer : answers) {

                    if (answer.getSelectedAnswer()
                            .equalsIgnoreCase(
                                    answer.getQuestion()
                                            .getCorrectAnswer()
                            )) {

                        score += 20;
                    }
                }

                session.setScore(score);

                session.setStatus(
                        ExamStatus.FINISHED
                );

                examSessionRepository.save(
                        session
                );

                studentService.updateStudentScore(
                        session.getStudent().getId(),
                        score
                );

                auditLogService.log(
                        session.getStudent().getId(),
                        "EXAM_AUTO_FINISHED"
                );
            }
        }
    }
}