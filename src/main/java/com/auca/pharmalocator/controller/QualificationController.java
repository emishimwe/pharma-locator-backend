package com.auca.pharmalocator.controller;

import com.auca.pharmalocator.model.Qualification;
import com.auca.pharmalocator.service.QualificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/qualifications")
public class QualificationController {
    private final QualificationService service;

    public QualificationController(QualificationService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Qualification qualification) {
        String msg = service.save(qualification);
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @GetMapping("/all")
    public List<Qualification> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Qualification> qualification = service.getById(id);
        if (qualification.isPresent()) {
            return ResponseEntity.ok(qualification.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The qualification with that id does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Qualification qualification) {
        String msg = service.update(id, qualification);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String msg = service.delete(id);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/unused")
    public List<Qualification> getUnused() {
        return service.findUnusedQualifications();
    }
}
