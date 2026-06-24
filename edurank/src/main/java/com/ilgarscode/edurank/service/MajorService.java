package com.ilgarscode.edurank.service;

import com.ilgarscode.edurank.entity.Major;
import com.ilgarscode.edurank.repository.MajorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorService {

    private final MajorRepository repository;

    public MajorService(MajorRepository repository) {
        this.repository = repository;
    }

    public Major save(Major major) {
        return repository.save(major);
    }

    public List<Major> getAll() {
        return repository.findAll();
    }
    public Major update(
            Long id,
            Major updatedMajor
    ) {

        Major major = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Major tapılmadı"));

        major.setName(
                updatedMajor.getName()
        );

        major.setQuota(
                updatedMajor.getQuota()
        );

        return repository.save(major);
    }

    public void delete(Long id) {

        Major major = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Major tapılmadı"));

        repository.delete(major);
    }
}