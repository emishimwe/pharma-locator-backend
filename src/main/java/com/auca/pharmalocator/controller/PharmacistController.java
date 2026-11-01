package com.auca.pharmalocator.controller;

import com.auca.pharmalocator.model.Pharmacist;
import com.auca.pharmalocator.service.PharmacistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/pharmacists")
public class PharmacistController {
    private final PharmacistService service;

    public PharmacistController(PharmacistService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Pharmacist pharmacist) {
        String msg = service.save(pharmacist);
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @GetMapping("/all")
    public List<Pharmacist> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Pharmacist> pharmacist = service.getById(id);
        if (pharmacist.isPresent()) {
            return ResponseEntity.ok(pharmacist.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The pharmacist with that id does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Pharmacist pharmacist) {
        String msg = service.update(id, pharmacist);
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

    @GetMapping("/by-qualification")
    public List<Pharmacist> getByQualificationName(@RequestParam String name) {
        return service.findByQualificationNameContainingIgnoreCase(name);
    }

    @PutMapping("/{pharmacistId}/qualifications/{qualificationId}")
    public ResponseEntity<String> addQualification(@PathVariable UUID pharmacistId, @PathVariable UUID qualificationId) {
        String msg = service.addQualification(pharmacistId, qualificationId);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.contains("already has that qualification")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    @PatchMapping("/{pharmacistId}/pharmacy")
    public ResponseEntity<String> assignPharmacy(@PathVariable UUID pharmacistId, @RequestParam int licenseNumber) {
        String msg = service.assignPharmacy(pharmacistId, licenseNumber);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.contains("taken by another")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.ok(msg);
    }
}
