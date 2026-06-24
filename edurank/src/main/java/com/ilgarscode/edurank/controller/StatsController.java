package com.ilgarscode.edurank.controller;

import com.ilgarscode.edurank.dto.stats.StatsDto;
import com.ilgarscode.edurank.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService service;

    public StatsController(
            StatsService service
    ) {
        this.service = service;
    }

    @GetMapping
    public StatsDto getStats() {
        return service.getStats();
    }
}