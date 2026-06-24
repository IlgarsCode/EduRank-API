package com.ilgarscode.edurank.controller;

import com.ilgarscode.edurank.entity.ExamConfig;
import com.ilgarscode.edurank.service.ExamConfigService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exam-config")
public class ExamConfigController {

    private final ExamConfigService service;

    public ExamConfigController(
            ExamConfigService service
    ) {
        this.service = service;
    }

    @GetMapping
    public ExamConfig getConfig() {
        return service.getConfig();
    }

    @PutMapping
    public ExamConfig update(
            @RequestBody ExamConfig config
    ) {
        return service.save(config);
    }
}