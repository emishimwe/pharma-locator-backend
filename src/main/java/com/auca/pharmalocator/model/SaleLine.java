package com.auca.pharmalocator.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "sale_line")
public class SaleLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String drugName;
    private String dosageNote;
    private int quantity;
    private LocalDate soldDate;

    @ManyToOne
    @JoinColumn(name = "drug_request_id")
    private DrugRequest drugRequest;

    public SaleLine() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDosageNote() {
        return dosageNote;
    }

    public void setDosageNote(String dosageNote) {
        this.dosageNote = dosageNote;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(LocalDate soldDate) {
        this.soldDate = soldDate;
    }

    public DrugRequest getDrugRequest() {
        return drugRequest;
    }

    public void setDrugRequest(DrugRequest drugRequest) {
        this.drugRequest = drugRequest;
    }
}
