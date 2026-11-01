package com.auca.pharmalocator.service;

import com.auca.pharmalocator.model.RequestStatus;
import com.auca.pharmalocator.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {
    private final CustomerRepository customerRepository;
    private final PharmacyRepository pharmacyRepository;
    private final PharmacistRepository pharmacistRepository;
    private final DrugRequestRepository drugRequestRepository;
    private final SaleLineRepository saleLineRepository;

    public DashboardService(CustomerRepository customerRepository,
                           PharmacyRepository pharmacyRepository,
                           PharmacistRepository pharmacistRepository,
                           DrugRequestRepository drugRequestRepository,
                           SaleLineRepository saleLineRepository) {
        this.customerRepository = customerRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.pharmacistRepository = pharmacistRepository;
        this.drugRequestRepository = drugRequestRepository;
        this.saleLineRepository = saleLineRepository;
    }

    public Map<String, Long> getSummary() {
        LinkedHashMap<String, Long> summary = new LinkedHashMap<>();
        summary.put("customers", customerRepository.count());
        summary.put("pharmacists", pharmacistRepository.count());
        summary.put("pharmaciesWithoutPharmacist", (long) pharmacyRepository.findFreePharmacies().size());
        summary.put("requestsToday", drugRequestRepository.countByRequestDate(LocalDate.now()));
        summary.put("submitted", drugRequestRepository.countByStatus(RequestStatus.SUBMITTED));
        summary.put("saleLines", saleLineRepository.count());
        return summary;
    }
}
