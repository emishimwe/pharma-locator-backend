package com.auca.pharmalocator.service;

import com.auca.pharmalocator.model.Customer;
import com.auca.pharmalocator.repository.CustomerRepository;
import com.auca.pharmalocator.repository.PharmacistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository repository;
    private final PharmacistRepository pharmacistRepository;

    public CustomerService(CustomerRepository repository, PharmacistRepository pharmacistRepository) {
        this.repository = repository;
        this.pharmacistRepository = pharmacistRepository;
    }

    public String save(Customer customer) {
        repository.save(customer);
        return "Customer saved successfully";
    }

    public List<Customer> getAll() {
        return repository.findAll();
    }

    public Optional<Customer> getById(UUID id) {
        return repository.findById(id);
    }

    public String update(UUID id, Customer updated) {
        Optional<Customer> existingOpt = repository.findById(id);
        if (existingOpt.isEmpty()) {
            return "The customer with that id does not exist";
        }

        Customer existing = existingOpt.get();
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setCustomerCode(updated.getCustomerCode());
        existing.setPhone(updated.getPhone());
        existing.setDistrict(updated.getDistrict());
        repository.save(existing);
        return "Customer updated successfully";
    }

    public String delete(UUID id) {
        if (!repository.existsById(id)) {
            return "The customer with that id does not exist";
        }
        repository.deleteById(id);
        return "Customer deleted successfully";
    }

    public List<Customer> findByDistrict(String district) {
        return repository.findByDistrictIgnoreCaseOrderByLastNameAsc(district);
    }

    public ResponseEntity<?> findCustomersByPharmacistId(UUID pharmacistId) {
        if (!pharmacistRepository.existsById(pharmacistId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The pharmacist with that id does not exist");
        }
        List<Customer> customers = repository.findCustomersByPharmacistId(pharmacistId);
        return ResponseEntity.ok(customers);
    }
}
