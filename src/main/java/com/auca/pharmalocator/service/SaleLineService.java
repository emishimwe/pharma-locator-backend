package com.auca.pharmalocator.service;

import com.auca.pharmalocator.model.DrugRequest;
import com.auca.pharmalocator.model.RequestStatus;
import com.auca.pharmalocator.model.SaleLine;
import com.auca.pharmalocator.repository.DrugRequestRepository;
import com.auca.pharmalocator.repository.SaleLineRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SaleLineService {
    private final SaleLineRepository saleLineRepository;
    private final DrugRequestRepository drugRequestRepository;

    public SaleLineService(SaleLineRepository saleLineRepository, DrugRequestRepository drugRequestRepository) {
        this.saleLineRepository = saleLineRepository;
        this.drugRequestRepository = drugRequestRepository;
    }

    public String save(UUID drugRequestId, SaleLine saleLine) {
        Optional<DrugRequest> requestOpt = drugRequestRepository.findById(drugRequestId);
        if (requestOpt.isEmpty()) {
            return "The drug request with that id does not exist";
        }

        DrugRequest request = requestOpt.get();
        if (request.getStatus() != RequestStatus.COLLECTED) {
            return "Sale lines are only for COLLECTED drug requests";
        }

        saleLine.setDrugRequest(request);
        saleLine.setSoldDate(LocalDate.now());
        saleLineRepository.save(saleLine);
        return "Sale line saved successfully";
    }

    public List<SaleLine> getAll() {
        return saleLineRepository.findAll();
    }

    public Optional<SaleLine> getById(UUID id) {
        return saleLineRepository.findById(id);
    }

    public List<SaleLine> findByCustomerCode(String customerCode) {
        return saleLineRepository.findByCustomerCodeOrderBySoldDateDesc(customerCode);
    }

    public String delete(UUID id) {
        if (!saleLineRepository.existsById(id)) {
            return "The sale line with that id does not exist";
        }
        saleLineRepository.deleteById(id);
        return "Sale line deleted successfully";
    }
}
