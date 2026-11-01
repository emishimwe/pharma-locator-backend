package com.auca.pharmalocator.repository;

import com.auca.pharmalocator.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    List<Customer> findByDistrictIgnoreCaseOrderByLastNameAsc(String district);

    @Query("SELECT DISTINCT d.customer FROM DrugRequest d WHERE d.pharmacist.id = :pharmacistId")
    List<Customer> findCustomersByPharmacistId(@Param("pharmacistId") UUID pharmacistId);
}
