package com.auca.pharmalocator.service;

import com.auca.pharmalocator.model.Pharmacy;
import com.auca.pharmalocator.repository.PharmacyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PharmacyService {
    private final PharmacyRepository repository;

    public PharmacyService(PharmacyRepository repository) {
        this.repository = repository;
    }

    public String save(Pharmacy pharmacy) {
        repository.save(pharmacy);
        return "Pharmacy saved successfully";
    }

    public List<Pharmacy> getAll() {
        return repository.findAll();
    }

    public Optional<Pharmacy> getById(UUID id) {
        return repository.findById(id);
    }

    public String update(UUID id, Pharmacy updated) {
        Optional<Pharmacy> existingOpt = repository.findById(id);
        if (existingOpt.isEmpty()) {
            return "The pharmacy with that id does not exist";
        }

        Pharmacy existing = existingOpt.get();
        existing.setLicenseNumber(updated.getLicenseNumber());
        existing.setName(updated.getName());
        repository.save(existing);
        return "Pharmacy updated successfully";
    }

    public String delete(UUID id) {
        if (!repository.existsById(id)) {
            return "The pharmacy with that id does not exist";
        }
        repository.deleteById(id);
        return "Pharmacy deleted successfully";
    }

    public List<Pharmacy> findFreePharmacies() {
        return repository.findFreePharmacies();
    }

    public Optional<Pharmacy> findByLicenseNumber(int licenseNumber) {
        return repository.findByLicenseNumber(licenseNumber);
    }
}
