package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public ResponseEntity<?> register(Customer customer) {
        // Kiểm tra xem username hoặc email đã tồn tại chưa
        if (customerRepository.existsByUsername(customer.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Customer already exists"));
        } else if (customerRepository.existsByEmail(customer.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
        }

        // Lưu khách hàng vào cơ sở dữ liệu
        customerRepository.save(customer);
        return ResponseEntity.ok(Map.of("message", "success"));
    }
}
