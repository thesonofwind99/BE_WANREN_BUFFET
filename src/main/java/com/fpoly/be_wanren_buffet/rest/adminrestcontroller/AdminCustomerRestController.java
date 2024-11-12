package com.fpoly.be_wanren_buffet.rest.adminrestcontroller;

import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/Customer")
public class AdminCustomerRestController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        log.info("Creating customer: {}", customer);
        customer.setCreatedDate(LocalDateTime.now());
        return new ResponseEntity<>(customerRepository.save(customer), HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        return customerRepository.findById(id).map(existingCustomer -> {
            if (applyUpdates(existingCustomer, updatedCustomer)) {
                existingCustomer.setUpdatedDate(LocalDateTime.now());
                customerRepository.save(existingCustomer);
                log.info("Customer with id: {} updated successfully", id);
                return ResponseEntity.ok("Customer updated successfully");
            }
            log.info("No changes made for customer with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("No changes detected");
        }).orElseGet(() -> {
            log.warn("Customer with id: {} not found", id);
            return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
        });
    }

    private boolean applyUpdates(Customer existingCustomer, Customer updatedCustomer) {
        return updateField(existingCustomer::setUsername, existingCustomer.getUsername(), updatedCustomer.getUsername()) |
                updateField(existingCustomer::setPassword, existingCustomer.getPassword(), updatedCustomer.getPassword()) |
                updateField(existingCustomer::setFullName, existingCustomer.getFullName(), updatedCustomer.getFullName()) |
                updateField(existingCustomer::setEmail, existingCustomer.getEmail(), updatedCustomer.getEmail()) |
                updateField(existingCustomer::setPhoneNumber, existingCustomer.getPhoneNumber(), updatedCustomer.getPhoneNumber()) |
                updateField(existingCustomer::setAddress, existingCustomer.getAddress(), updatedCustomer.getAddress()) |
                updateField(existingCustomer::setLoyaltyPoints, existingCustomer.getLoyaltyPoints(), updatedCustomer.getLoyaltyPoints()) |
                updateField(existingCustomer::setCustomerType, existingCustomer.getCustomerType(), updatedCustomer.getCustomerType()) |
                updateField(existingCustomer::setAccountStatus, existingCustomer.getAccountStatus(), updatedCustomer.getAccountStatus());
    }

    private <T> boolean updateField(Consumer<T> setter, T currentValue, T newValue) {
        if (newValue != null && !newValue.equals(currentValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }

    @PatchMapping("/updateAccountStatus/{id}")
    public ResponseEntity<String> updateAccountStatus(@PathVariable Long id, @RequestBody Boolean accountStatus) {
        return customerRepository.findById(id).map(existingCustomer -> {
            existingCustomer.setAccountStatus(accountStatus);
            existingCustomer.setUpdatedDate(LocalDateTime.now());
            customerRepository.save(existingCustomer);
            log.info("Account status for customer with id: {} updated successfully", id);
            return ResponseEntity.ok("Account status updated successfully");
        }).orElseGet(() -> {
            log.warn("Customer with id: {} not found", id);
            return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
        });
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            customerRepository.deleteById(id);
            log.info("Customer with id: {} deleted successfully", id);
            return ResponseEntity.ok("Customer deleted successfully");
        } else {
            log.warn("Customer with id: {} not found", id);
            return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchCustomersByFullName(@RequestParam String fullName) {
        String normalizedFullName = normalizeString(fullName);
        List<Customer> customers = customerRepository.findAll().stream()
                .filter(customer -> normalizeString(customer.getFullName()).contains(normalizedFullName))
                .collect(Collectors.toList());

        if (customers.isEmpty()) {
            log.info("No customers found with the full name containing: {}", fullName);
            return new ResponseEntity<>("No customers found", HttpStatus.NOT_FOUND);
        }
        log.info("Found {} customers with the full name containing: {}", customers.size(), fullName);
        return ResponseEntity.ok(customers);
    }

    // Utility method to normalize strings
    private String normalizeString(String input) {
        if (input == null) return null;
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(); // Normalize and remove accents, convert to lowercase
    }

}


