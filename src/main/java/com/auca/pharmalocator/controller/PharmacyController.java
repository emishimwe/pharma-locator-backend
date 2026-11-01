package com.auca.pharmalocator.controller;

import com.auca.pharmalocator.model.Pharmacy;
import com.auca.pharmalocator.service.PharmacyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/pharmacies")
public class PharmacyController {
    private final PharmacyService service;

    public PharmacyController(PharmacyService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Pharmacy pharmacy) {
        String msg = service.save(pharmacy);
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @GetMapping("/all")
    public List<Pharmacy> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Pharmacy> pharmacy = service.getById(id);
        if (pharmacy.isPresent()) {
            return ResponseEntity.ok(pharmacy.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The pharmacy with that id does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Pharmacy pharmacy) {
        String msg = service.update(id, pharmacy);
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

    @GetMapping("/free")
    public List<Pharmacy> getFreePharmacies() {
        return service.findFreePharmacies();
    }
}
