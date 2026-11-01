package com.auca.pharmalocator.service;

import com.auca.pharmalocator.model.Qualification;
import com.auca.pharmalocator.repository.QualificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QualificationService {
    private final QualificationRepository repository;

    public QualificationService(QualificationRepository repository) {
        this.repository = repository;
    }

    public String save(Qualification qualification) {
        repository.save(qualification);
        return "Qualification saved successfully";
    }

    public List<Qualification> getAll() {
        return repository.findAll();
    }

    public Optional<Qualification> getById(UUID id) {
        return repository.findById(id);
    }

    public String update(UUID id, Qualification updated) {
        Optional<Qualification> existingOpt = repository.findById(id);
        if (existingOpt.isEmpty()) {
            return "The qualification with that id does not exist";
        }

        Qualification existing = existingOpt.get();
        existing.setName(updated.getName());
        repository.save(existing);
        return "Qualification updated successfully";
    }

    public String delete(UUID id) {
        if (!repository.existsById(id)) {
            return "The qualification with that id does not exist";
        }
        repository.deleteById(id);
        return "Qualification deleted successfully";
    }

    public List<Qualification> findUnusedQualifications() {
        return repository.findUnusedQualifications();
    }
}
