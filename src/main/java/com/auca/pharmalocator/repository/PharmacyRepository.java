package com.auca.pharmalocator.repository;

import com.auca.pharmalocator.model.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PharmacyRepository extends JpaRepository<Pharmacy, UUID> {
    @Query("SELECT l FROM Pharmacy l WHERE NOT EXISTS (SELECT p FROM Pharmacist p WHERE p.pharmacy = l) ORDER BY l.licenseNumber ASC")
    List<Pharmacy> findFreePharmacies();

    Optional<Pharmacy> findByLicenseNumber(int licenseNumber);
}
