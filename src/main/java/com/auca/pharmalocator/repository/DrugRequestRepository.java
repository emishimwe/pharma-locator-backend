package com.auca.pharmalocator.repository;

import com.auca.pharmalocator.model.DrugRequest;
import com.auca.pharmalocator.model.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DrugRequestRepository extends JpaRepository<DrugRequest, UUID> {
    List<DrugRequest> findByStatusOrderByRequestDateAsc(RequestStatus status);

    List<DrugRequest> findByRequestDateBetweenOrderByRequestDateAsc(LocalDate start, LocalDate end);

    boolean existsByPharmacistIdAndRequestDateAndStatusNot(UUID pharmacistId, LocalDate requestDate, RequestStatus status);

    @Query("SELECT d.status, COUNT(d) FROM DrugRequest d GROUP BY d.status")
    List<Object[]> countDrugRequestsByStatus();

    @Modifying
    @Query("UPDATE DrugRequest d SET d.status = com.auca.pharmalocator.model.RequestStatus.DECLINED WHERE d.pharmacist.id = :pharmacistId AND d.requestDate = :date AND d.status <> com.auca.pharmalocator.model.RequestStatus.COLLECTED")
    @Transactional
    int cancelDayForPharmacist(@Param("pharmacistId") UUID pharmacistId, @Param("date") LocalDate date);

    long countByRequestDate(LocalDate date);

    long countByStatus(RequestStatus status);

    Page<DrugRequest> findAll(Pageable pageable);
}
