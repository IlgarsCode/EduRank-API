package com.ilgarscode.edurank.entity;

import java.time.LocalDateTime;
import java.util.List;

public class ExamWindowDto {

    private Long sessionId;

    private ExamStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime endsAt;

    private long remainingSeconds;

    private int totalQuestions;

    private List<Question> questions;

    public ExamWindowDto(
            Long sessionId,
            ExamStatus status,
            LocalDateTime startedAt,
            LocalDateTime endsAt,
            long remainingSeconds,
            int totalQuestions,
            List<Question> questions
    ) {
        this.sessionId = sessionId;
        this.status = status;
        this.startedAt = startedAt;
        this.endsAt = endsAt;
        this.remainingSeconds = remainingSeconds;
        this.totalQuestions = totalQuestions;
        this.questions = questions;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public ExamStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public long getRemainingSeconds() {
        return remainingSeconds;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}