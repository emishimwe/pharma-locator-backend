package com.auca.pharmalocator.controller;

import com.auca.pharmalocator.model.SaleLine;
import com.auca.pharmalocator.service.SaleLineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/sale-lines")
public class SaleLineController {
    private final SaleLineService service;

    public SaleLineController(SaleLineService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam UUID drugRequestId, @RequestBody SaleLine saleLine) {
        String msg = service.save(drugRequestId, saleLine);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (msg.equals("Sale lines are only for COLLECTED drug requests")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @GetMapping("/all")
    public List<SaleLine> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<SaleLine> saleLine = service.getById(id);
        if (saleLine.isPresent()) {
            return ResponseEntity.ok(saleLine.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The sale line with that id does not exist");
    }

    @GetMapping("/customer/{customerCode}")
    public List<SaleLine> getByCustomerCode(@PathVariable String customerCode) {
        return service.findByCustomerCode(customerCode);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String msg = service.delete(id);
        if (msg.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(msg);
    }
}
