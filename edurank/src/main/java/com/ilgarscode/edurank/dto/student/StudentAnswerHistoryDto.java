package com.ilgarscode.edurank.dto.student;

public class StudentAnswerHistoryDto {

    private Long questionId;
    private String questionText;

    private String selectedAnswer;
    private String correctAnswer;

    private boolean correct;

    public StudentAnswerHistoryDto(
            Long questionId,
            String questionText,
            String selectedAnswer,
            String correctAnswer,
            boolean correct
    ) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.selectedAnswer = selectedAnswer;
        this.correctAnswer = correctAnswer;
        this.correct = correct;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }
}