package com.auca.pharmalocator.controller;

import com.auca.pharmalocator.model.DrugRequest;
import com.auca.pharmalocator.model.RequestStatus;
import com.auca.pharmalocator.service.DrugRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/drug-requests")
public class DrugRequestController {
    private final DrugRequestService service;

    public DrugRequestController(DrugRequestService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody DrugRequest request) {
        String msg = service.save(request);
        if (msg.equals("Drug request date cannot be in the past")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        if (msg.equals("Pharmacist already has a drug request on that date")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @GetMapping("/all")
    public List<DrugRequest> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<DrugRequest> result = service.getById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The drug request with that id does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody DrugRequest request) {
        String msg = service.update(id, request);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable UUID id, @RequestParam RequestStatus status) {
        String msg = service.updateStatus(id, status);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.startsWith("Cannot change status")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String msg = service.delete(id);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.contains("has sale lines")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/by-status")
    public List<DrugRequest> getByStatus(@RequestParam RequestStatus status) {
        return service.findByStatus(status);
    }

    @GetMapping("/between")
    public List<DrugRequest> getBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return service.findBetween(startDate, endDate);
    }

    @GetMapping("/stats/by-status")
    public List<Object[]> getStatsByStatus() {
        return service.getStatsByStatus();
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam UUID pharmacistId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        String msg = service.cancelDayForPharmacist(pharmacistId, localDate);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/page")
    public Page<DrugRequest> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "requestDate,desc") String[] sort) {
        String sortField = sort[0];
        Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        return service.getPage(pageable);
    }
}
