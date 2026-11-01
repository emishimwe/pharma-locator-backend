package com.auca.pharmalocator.repository;

import com.auca.pharmalocator.model.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface QualificationRepository extends JpaRepository<Qualification, UUID> {
    @Query("SELECT q FROM Qualification q WHERE q.pharmacists IS EMPTY")
    List<Qualification> findUnusedQualifications();
}
