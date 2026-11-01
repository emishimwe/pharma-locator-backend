package com.auca.pharmalocator.repository;

import com.auca.pharmalocator.model.DrugRequest;
import com.auca.pharmalocator.model.SaleLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SaleLineRepository extends JpaRepository<SaleLine, UUID> {
    @Query("SELECT s FROM SaleLine s WHERE s.drugRequest.customer.customerCode = :customerCode ORDER BY s.soldDate DESC")
    List<SaleLine> findByCustomerCodeOrderBySoldDateDesc(@Param("customerCode") String customerCode);

    boolean existsByDrugRequest(DrugRequest drugRequest);
}
