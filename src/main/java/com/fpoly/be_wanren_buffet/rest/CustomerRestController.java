// src/main/java/com/fpoly/be_wanren_buffet/rest/CustomerRestController.java

package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.dto.UpdateCustomerDTO;
import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.service.CustomerService;
import com.fpoly.be_wanren_buffet.security.JwtResponse;
import com.fpoly.be_wanren_buffet.security.LoginRequest;
import com.fpoly.be_wanren_buffet.service.CustomerAuthService;
import com.fpoly.be_wanren_buffet.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/customer")
public class CustomerRestController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerAuthService customerAuthService;

    @Autowired
    private JwtService jwtService;

    @CrossOrigin("http://localhost:3000")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody Customer customer) {

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerService.register(customer);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            if (authentication.isAuthenticated()) {
                // Lấy thông tin người dùng đã đăng nhập
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                // Giả sử bạn có thể lấy fullName từ UserDetails hoặc từ repository
                Customer authenticatedCustomer = customerAuthService.authenticate(loginRequest.getUsername());
                String fullName = authenticatedCustomer.getFullName();
                System.out.println(fullName + "AAAAA");
                String email = authenticatedCustomer.getEmail();
                String phone = authenticatedCustomer.getPhoneNumber();
                final String jwt = jwtService.generateToken(userDetails, fullName , email, phone);
                return ResponseEntity.ok(new JwtResponse(jwt));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Fall succescc");
    }

    @PutMapping("/updateCustomer/{username}")
    public ResponseEntity<?> updateCustomer(
            @PathVariable String username,
            @Validated @RequestBody UpdateCustomerDTO updateCustomerDTO) {

            Customer customer = customerService.findCustomerByUsername(username);
            UpdateCustomerDTO updateCustomerDTO1 = new UpdateCustomerDTO();
            System.out.println(customer);
            customer.setFullName(updateCustomerDTO.getFullName());
            customer.setEmail(updateCustomerDTO.getEmail());
            customer.setPhoneNumber(updateCustomerDTO.getPhoneNumber());
            customerService.save(customer);
        UserDetails userDetails = customerAuthService.loadUserByUsername(username);
        String newToken = jwtService.generateToken(userDetails, customer.getFullName(), customer.getEmail(), customer.getPhoneNumber());
        System.out.println(customer.getPhoneNumber());
        updateCustomerDTO1.setFullName(customer.getFullName());
        updateCustomerDTO1.setEmail(customer.getEmail());
        updateCustomerDTO1.setPhoneNumber(updateCustomerDTO.getPhoneNumber());
        updateCustomerDTO1.setJwtToken(newToken);
        return ResponseEntity.ok().body(updateCustomerDTO1);
    }
}
