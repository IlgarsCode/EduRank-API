package com.ilgarscode.edurank.service;

import com.ilgarscode.edurank.entity.Question;
import com.ilgarscode.edurank.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository repo;

    public QuestionService(QuestionRepository repo) {
        this.repo = repo;
    }

    public Question addQuestion(Question q) {
        return repo.save(q);
    }

    public List<Question> getAll() {
        return repo.findAll();
    }
    public Question updateQuestion(
            Long id,
            Question updatedQuestion
    ) {

        Question question = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Question tapılmadı"));

        question.setSubject(updatedQuestion.getSubject());
        question.setText(updatedQuestion.getText());

        question.setOptionA(updatedQuestion.getOptionA());
        question.setOptionB(updatedQuestion.getOptionB());
        question.setOptionC(updatedQuestion.getOptionC());
        question.setOptionD(updatedQuestion.getOptionD());

        question.setCorrectAnswer(
                updatedQuestion.getCorrectAnswer()
        );

        return repo.save(question);
    }

    public void deleteQuestion(Long id) {

        Question question = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Question tapılmadı"));

        repo.delete(question);
    }
}