package com.ilgarscode.edurank.service;

import com.ilgarscode.edurank.entity.ExamConfig;
import com.ilgarscode.edurank.repository.ExamConfigRepository;
import org.springframework.stereotype.Service;

@Service
public class ExamConfigService {

    private final ExamConfigRepository repository;

    public ExamConfigService(
            ExamConfigRepository repository
    ) {
        this.repository = repository;
    }

    public ExamConfig getConfig() {

        return repository.findAll()
                .stream()
                .findFirst()
                .orElseGet(() -> {

                    ExamConfig config =
                            new ExamConfig();

                    config.setQuestionCount(5);
                    config.setDurationMinutes(1);
                    config.setPassingScore(50);

                    return repository.save(config);
                });
    }

    public ExamConfig save(
            ExamConfig config
    ) {

        ExamConfig current =
                getConfig();

        current.setQuestionCount(
                config.getQuestionCount()
        );

        current.setDurationMinutes(
                config.getDurationMinutes()
        );

        current.setPassingScore(
                config.getPassingScore()
        );

        return repository.save(current);
    }
}