package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.security.JwtResponse;
import com.fpoly.be_wanren_buffet.security.LoginRequest;
import com.fpoly.be_wanren_buffet.service.CustomerAuthService;
import com.fpoly.be_wanren_buffet.service.CustomerService;
import com.fpoly.be_wanren_buffet.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            if (authentication.isAuthenticated()) {
                // Lấy thông tin người dùng đã đăng nhập
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                // Giả sử bạn có thể lấy fullName từ UserDetails hoặc từ repository
                String fullName = customerAuthService.authenticate(loginRequest.getUsername()).getFullname();
                final String jwt = jwtService.generateToken(userDetails, fullName);
                return ResponseEntity.ok(new JwtResponse(jwt));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Fall succescc");
    }

}
