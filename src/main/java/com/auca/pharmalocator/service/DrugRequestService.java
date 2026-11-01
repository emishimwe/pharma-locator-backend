package com.auca.pharmalocator.service;

import com.auca.pharmalocator.model.DrugRequest;
import com.auca.pharmalocator.model.RequestStatus;
import com.auca.pharmalocator.repository.DrugRequestRepository;
import com.auca.pharmalocator.repository.SaleLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DrugRequestService {
    private final DrugRequestRepository repository;
    private final SaleLineRepository saleLineRepository;

    public DrugRequestService(DrugRequestRepository repository, SaleLineRepository saleLineRepository) {
        this.repository = repository;
        this.saleLineRepository = saleLineRepository;
    }

    public String save(DrugRequest request) {
        if (request.getRequestDate() != null && request.getRequestDate().isBefore(LocalDate.now())) {
            return "Drug request date cannot be in the past";
        }

        if (request.getPharmacist() != null && request.getPharmacist().getId() != null) {
            boolean hasConflict = repository.existsByPharmacistIdAndRequestDateAndStatusNot(
                    request.getPharmacist().getId(),
                    request.getRequestDate(),
                    RequestStatus.DECLINED
            );
            if (hasConflict) {
                return "Pharmacist already has a drug request on that date";
            }
        }

        request.setStatus(RequestStatus.SUBMITTED);
        repository.save(request);
        return "Drug request saved successfully";
    }

    public List<DrugRequest> getAll() {
        return repository.findAll();
    }

    public Optional<DrugRequest> getById(UUID id) {
        return repository.findById(id);
    }

    public String update(UUID id, DrugRequest updated) {
        Optional<DrugRequest> existingOpt = repository.findById(id);
        if (existingOpt.isEmpty()) {
            return "The drug request with that id does not exist";
        }

        DrugRequest existing = existingOpt.get();
        existing.setRequestDate(updated.getRequestDate());
        existing.setDrugName(updated.getDrugName());
        existing.setCustomer(updated.getCustomer());
        existing.setPharmacist(updated.getPharmacist());
        repository.save(existing);
        return "Drug request updated successfully";
    }

    public String updateStatus(UUID id, RequestStatus newStatus) {
        Optional<DrugRequest> existingOpt = repository.findById(id);
        if (existingOpt.isEmpty()) {
            return "The drug request with that id does not exist";
        }

        DrugRequest request = existingOpt.get();
        RequestStatus currentStatus = request.getStatus();

        boolean allowed = false;
        if (currentStatus == RequestStatus.SUBMITTED
                && (newStatus == RequestStatus.RESERVED || newStatus == RequestStatus.DECLINED)) {
            allowed = true;
        } else if (currentStatus == RequestStatus.RESERVED
                && (newStatus == RequestStatus.COLLECTED || newStatus == RequestStatus.DECLINED)) {
            allowed = true;
        }

        if (!allowed) {
            return "Cannot change status from " + currentStatus + " to " + newStatus;
        }

        request.setStatus(newStatus);
        repository.save(request);
        return "Drug request status updated successfully";
    }

    public String delete(UUID id) {
        Optional<DrugRequest> existingOpt = repository.findById(id);
        if (existingOpt.isEmpty()) {
            return "The drug request with that id does not exist";
        }
        if (saleLineRepository.existsByDrugRequest(existingOpt.get())) {
            return "That drug request has sale lines, delete them first";
        }
        repository.deleteById(id);
        return "Drug request deleted successfully";
    }

    public List<DrugRequest> findByStatus(RequestStatus status) {
        return repository.findByStatusOrderByRequestDateAsc(status);
    }

    public List<DrugRequest> findBetween(LocalDate start, LocalDate end) {
        return repository.findByRequestDateBetweenOrderByRequestDateAsc(start, end);
    }

    public List<Object[]> getStatsByStatus() {
        return repository.countDrugRequestsByStatus();
    }

    @Transactional
    public String cancelDayForPharmacist(UUID pharmacistId, LocalDate date) {
        int count = repository.cancelDayForPharmacist(pharmacistId, date);
        return count + " drug requests cancelled";
    }

    public Page<DrugRequest> getPage(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
