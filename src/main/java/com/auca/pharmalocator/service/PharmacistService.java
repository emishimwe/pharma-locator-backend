package com.auca.pharmalocator.service;

import com.auca.pharmalocator.model.Pharmacist;
import com.auca.pharmalocator.model.Pharmacy;
import com.auca.pharmalocator.model.Qualification;
import com.auca.pharmalocator.repository.PharmacistRepository;
import com.auca.pharmalocator.repository.PharmacyRepository;
import com.auca.pharmalocator.repository.QualificationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PharmacistService {
    private final PharmacistRepository pharmacistRepository;
    private final PharmacyRepository pharmacyRepository;
    private final QualificationRepository qualificationRepository;

    public PharmacistService(PharmacistRepository pharmacistRepository,
                            PharmacyRepository pharmacyRepository,
                            QualificationRepository qualificationRepository) {
        this.pharmacistRepository = pharmacistRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.qualificationRepository = qualificationRepository;
    }

    public String save(Pharmacist pharmacist) {
        pharmacistRepository.save(pharmacist);
        return "Pharmacist saved successfully";
    }

    public List<Pharmacist> getAll() {
        return pharmacistRepository.findAll();
    }

    public Optional<Pharmacist> getById(UUID id) {
        return pharmacistRepository.findById(id);
    }

    public String update(UUID id, Pharmacist updated) {
        Optional<Pharmacist> existingOpt = pharmacistRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return "The pharmacist with that id does not exist";
        }

        Pharmacist existing = existingOpt.get();
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setPharmacy(updated.getPharmacy());
        existing.setQualifications(updated.getQualifications());
        pharmacistRepository.save(existing);
        return "Pharmacist updated successfully";
    }

    public String delete(UUID id) {
        if (!pharmacistRepository.existsById(id)) {
            return "The pharmacist with that id does not exist";
        }
        pharmacistRepository.deleteById(id);
        return "Pharmacist deleted successfully";
    }

    public List<Pharmacist> findByQualificationNameContainingIgnoreCase(String name) {
        return pharmacistRepository.findByQualificationNameContainingIgnoreCase(name);
    }

    public String addQualification(UUID pharmacistId, UUID qualificationId) {
        Optional<Pharmacist> pharmacistOpt = pharmacistRepository.findById(pharmacistId);
        if (pharmacistOpt.isEmpty()) {
            return "The pharmacist with that id does not exist";
        }

        Optional<Qualification> qualificationOpt = qualificationRepository.findById(qualificationId);
        if (qualificationOpt.isEmpty()) {
            return "That qualification does not exist";
        }

        Pharmacist pharmacist = pharmacistOpt.get();
        Qualification qualification = qualificationOpt.get();

        if (pharmacist.getQualifications() == null) {
            pharmacist.setQualifications(new ArrayList<>());
        }

        if (pharmacist.getQualifications().contains(qualification)) {
            return "The pharmacist already has that qualification";
        }

        pharmacist.getQualifications().add(qualification);
        pharmacistRepository.save(pharmacist);
        return "Qualification added to the pharmacist";
    }

    public String assignPharmacy(UUID pharmacistId, int licenseNumber) {
        Optional<Pharmacist> pharmacistOpt = pharmacistRepository.findById(pharmacistId);
        if (pharmacistOpt.isEmpty()) {
            return "The pharmacist with that id does not exist";
        }

        Optional<Pharmacy> pharmacyOpt = pharmacyRepository.findByLicenseNumber(licenseNumber);
        if (pharmacyOpt.isEmpty()) {
            return "The pharmacy with that licenseNumber does not exist";
        }

        Pharmacist pharmacist = pharmacistOpt.get();
        Pharmacy pharmacy = pharmacyOpt.get();

        boolean taken = pharmacistRepository.existsByPharmacyId(pharmacy.getId());
        if (taken && (pharmacist.getPharmacy() == null || !pharmacist.getPharmacy().getId().equals(pharmacy.getId()))) {
            return "That pharmacy is taken by another pharmacist";
        }

        pharmacist.setPharmacy(pharmacy);
        pharmacistRepository.save(pharmacist);
        return "Pharmacy changed successfully";
    }
}
