package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.dto.UpdateCustomerDTO;
import com.fpoly.be_wanren_buffet.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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


}
