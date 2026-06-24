package com.ilgarscode.edurank.service;

import com.ilgarscode.edurank.entity.ExamSession;
import com.ilgarscode.edurank.entity.ExamStatus;
import com.ilgarscode.edurank.repository.ExamSessionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AutoSubmitService {

    private final ExamSessionRepository examSessionRepository;

    public AutoSubmitService(ExamSessionRepository examSessionRepository) {
        this.examSessionRepository = examSessionRepository;
    }

    @Scheduled(fixedRate = 60000) // hər 1 dəqiqə
    public void autoSubmitExpiredExams() {

        List<ExamSession> sessions = examSessionRepository.findAll();

        for (ExamSession session : sessions) {

            if (session.getStatus() == ExamStatus.ACTIVE
                    && session.getEndsAt() != null
                    && session.getEndsAt().isBefore(LocalDateTime.now())) {

                session.setStatus(ExamStatus.FINISHED);

                examSessionRepository.save(session);

                System.out.println(
                        "Auto submitted session: " + session.getId()
                );
            }
        }
    }
}