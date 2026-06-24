package com.ilgarscode.edurank.controller;

import com.ilgarscode.edurank.entity.Major;
import com.ilgarscode.edurank.service.MajorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/majors")
public class MajorController {

    private final MajorService service;

    public MajorController(
            MajorService service
    ) {
        this.service = service;
    }

    @PostMapping
    public Major addMajor(
            @RequestBody Major major
    ) {
        return service.save(major);
    }

    @GetMapping
    public List<Major> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public Major update(
            @PathVariable Long id,
            @RequestBody Major major
    ) {

        return service.update(
                id,
                major
        );
    }

    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id
    ) {

        service.delete(id);

        return "Major silindi";
    }
}