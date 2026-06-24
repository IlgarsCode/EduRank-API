package com.ilgarscode.edurank.controller;

import com.ilgarscode.edurank.entity.Question;
import com.ilgarscode.edurank.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService service;

    public QuestionController(
            QuestionService service
    ) {
        this.service = service;
    }

    @PostMapping
    public Question add(
            @RequestBody Question q
    ) {
        return service.addQuestion(q);
    }

    @GetMapping
    public List<Question> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public Question update(
            @PathVariable Long id,
            @RequestBody Question question
    ) {

        return service.updateQuestion(
                id,
                question
        );
    }

    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id
    ) {

        service.deleteQuestion(id);

        return "Question silindi";
    }
}