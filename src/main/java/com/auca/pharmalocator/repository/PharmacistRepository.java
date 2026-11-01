package com.auca.pharmalocator.repository;

import com.auca.pharmalocator.model.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PharmacistRepository extends JpaRepository<Pharmacist, UUID> {
    @Query("SELECT DISTINCT p FROM Pharmacist p JOIN p.qualifications q WHERE LOWER(q.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Pharmacist> findByQualificationNameContainingIgnoreCase(@Param("name") String name);

    boolean existsByPharmacyId(UUID pharmacyId);
}
