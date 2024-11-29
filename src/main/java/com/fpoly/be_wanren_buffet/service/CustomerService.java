package com.fpoly.be_wanren_buffet.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fpoly.be_wanren_buffet.dto.LoyaltyPointsResponse;
import com.fpoly.be_wanren_buffet.dto.UpdateLoyaltyPointsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.entity.Customer;

@Service
public class CustomerService {
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private CustomerRepository customerRepository;

    public ResponseEntity<?> register(Customer customer) {
        // Kiểm tra xem username hoặc email đã tồn tại chưa
        if (customerRepository.existsByUsername(customer.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Customer already exists"));
        } else if (customerRepository.existsByEmail(customer.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
        } else if (customerRepository.existsByEmail(customer.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(Map.of("message", "PhoneNumber already exists"));
        }

        // Lưu khách hàng vào cơ sở dữ liệu
        customerRepository.save(customer);
        return ResponseEntity.ok(Map.of("message", "success"));
    }

    public Customer findCustomerByUsername(String username) {
        List<Customer> customers = customerRepository.findAll();
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        return null;
    }

    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    public Optional<Customer> findByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    public void updatePassword(Customer user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(user);
    }

    public LoyaltyPointsResponse getLoyaltyPointsByPhoneNumber(String phoneNumber) {
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber);

        return new LoyaltyPointsResponse(phoneNumber, customer.getLoyaltyPoints());
    }

    public LoyaltyPointsResponse updateLoyaltyPoints(UpdateLoyaltyPointsRequest request) {
        Customer customer = customerRepository.findByPhoneNumber(request.getPhoneNumber());
//                .orElseThrow(() -> new RuntimeException("Customer not found with phone number: " + request.getPhoneNumber()));

        if (customer.getLoyaltyPoints() < request.getPointsToDeduct()) {
            throw new IllegalArgumentException("Not enough loyalty points to deduct. Current points: " + customer.getLoyaltyPoints());
        }

        customer.setLoyaltyPoints(customer.getLoyaltyPoints() - request.getPointsToDeduct());
        customerRepository.save(customer);

        return new LoyaltyPointsResponse(customer.getPhoneNumber(), customer.getLoyaltyPoints());
    }


}
