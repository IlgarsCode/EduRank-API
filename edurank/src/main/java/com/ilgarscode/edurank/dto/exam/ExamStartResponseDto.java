package com.ilgarscode.edurank.dto.exam;

import com.ilgarscode.edurank.entity.Question;
import java.util.List;

public class ExamStartResponseDto {

    private Long sessionId;
    private List<Question> questions;

    public ExamStartResponseDto(Long sessionId, List<Question> questions) {
        this.sessionId = sessionId;
        this.questions = questions;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}