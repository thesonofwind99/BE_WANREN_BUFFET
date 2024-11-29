package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.dto.UpdateCustomerDTO;
import com.fpoly.be_wanren_buffet.dto.UpdatePhoneNumberDTO;
import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.security.JwtResponse;
import com.fpoly.be_wanren_buffet.security.LoginRequest;
import com.fpoly.be_wanren_buffet.service.CustomerAuthService;
import com.fpoly.be_wanren_buffet.service.CustomerForStaffService;
import com.fpoly.be_wanren_buffet.service.CustomerService;
import com.fpoly.be_wanren_buffet.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/customer")
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerForStaffService customerForStaffService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerAuthService customerAuthService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JwtService jwtService;

    /**
     * Endpoint đăng ký khách hàng
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody Customer customer) {
        log.info("Đăng ký khách hàng: {}", customer.getUsername());
        try {
            // Mã hóa mật khẩu trước khi lưu
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            // Đăng ký khách hàng thông qua service
            return customerService.register(customer);
        } catch (Exception e) {
            log.error("Lỗi khi đăng ký khách hàng: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Đăng ký thất bại. Vui lòng thử lại.");
        }
    }

    /**
     * Endpoint đăng nhập khách hàng
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginRequest) {
        log.info("Khách hàng đang cố gắng đăng nhập: {}", loginRequest.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            if (authentication.isAuthenticated()) {
                // Lấy thông tin người dùng đã đăng nhập
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                // Lấy thông tin khách hàng từ service
                Customer authenticatedCustomer = customerAuthService.authenticate(loginRequest.getUsername());
                if (authenticatedCustomer == null) {
                    log.warn("Khách hàng không tồn tại: {}", loginRequest.getUsername());
                    return ResponseEntity.badRequest().body("Thông tin đăng nhập không chính xác.");
                }

                String fullName = authenticatedCustomer.getFullName();
                String email = authenticatedCustomer.getEmail();
                String phone = authenticatedCustomer.getPhoneNumber();
                Long userId = authenticatedCustomer.getCustomerId();
                String address = authenticatedCustomer.getAddress();

                log.info("Đăng nhập thành công cho khách hàng: {}", email);

                // Tạo JWT token cho khách hàng
                final String jwt = jwtService.generateTokenForCustomer(fullName, email, phone, userId, address);
                return ResponseEntity.ok(new JwtResponse(jwt));
            }
        } catch (Exception e) {
            log.error("Lỗi khi đăng nhập: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Thông tin đăng nhập không chính xác.");
        }
        return ResponseEntity.badRequest().body("Đăng nhập thất bại.");
    }

    /**
     * Endpoint cập nhật thông tin khách hàng
     */
    @PutMapping("/updateCustomer/{username}")
    public ResponseEntity<?> updateCustomer(
            @PathVariable String username,
            @Validated @RequestBody UpdateCustomerDTO updateCustomerDTO) {
        log.info("Cập nhật thông tin khách hàng: {}", username);
        try {
            Customer customer = customerService.findCustomerByUsername(username);
            if (customer == null) {
                log.warn("Khách hàng không tồn tại: {}", username);
                throw new ResourceNotFoundException("Customer not found with username: " + username);
            }

            // Cập nhật các trường thông tin
            customer.setFullName(updateCustomerDTO.getFullName());
            customer.setEmail(updateCustomerDTO.getEmail());
            customer.setPhoneNumber(updateCustomerDTO.getPhoneNumber());
            // Bạn có thể thêm các trường khác nếu cần
            customerService.save(customer);

            // Tạo mới JWT token với thông tin cập nhật
            String newToken = jwtService.generateTokenForCustomer(
                    customer.getFullName(),
                    customer.getEmail(),
                    customer.getPhoneNumber(),
                    customer.getCustomerId(),
                    customer.getAddress()
            );

            // Chuẩn bị DTO phản hồi
            UpdateCustomerDTO responseDTO = new UpdateCustomerDTO();
            responseDTO.setFullName(customer.getFullName());
            responseDTO.setEmail(customer.getEmail());
            responseDTO.setPhoneNumber(customer.getPhoneNumber());
            responseDTO.setJwtToken(newToken);

            log.info("Cập nhật thành công cho khách hàng: {}", username);
            return ResponseEntity.ok().body(responseDTO);
        } catch (ResourceNotFoundException e) {
            log.error("Lỗi cập nhật khách hàng: {}", e.getMessage());
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi cập nhật khách hàng: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Cập nhật thất bại. Vui lòng thử lại.");
        }
    }

    @PutMapping("/updatePassword/{customerId}")
    public ResponseEntity<String> updatePassword(
            @PathVariable("customerId") String customerId,
            @RequestBody Map<String, String> requestBody) {

        // Lấy mật khẩu từ request body
        String password = requestBody.get("password");

        if (password == null || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is required");
        }

        // Fetch the customer using customerId
        Optional<Customer> optionalCustomer = customerRepository.findCustomerByCustomerId(Long.parseLong(customerId));

        if (!optionalCustomer.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        Customer customer = optionalCustomer.get();

        try {
            // Mã hóa và cập nhật mật khẩu
            customer.setPassword(passwordEncoder.encode(password));
            customerRepository.save(customer);  // Lưu thông tin khách hàng

            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating password");
        }
    }


    // Method to validate Vietnamese phone numbers




    /**
     * Endpoint cập nhật điểm thưởng cho khách hàng
     */
    @PutMapping("/loyal_point/{phone_number}/{total_amount}")
    public ResponseEntity<?> updateLoyaltyPoint(@PathVariable(name = "phone_number") String phoneNumber, @PathVariable(name = "total_amount") Double totalAmount){
        Map<String, Object> response = new HashMap<>();
        response.put("loyal_phone", customerForStaffService.updateloyalPointOfCustomer(phoneNumber, totalAmount));
        response.put("message", "Tích điểm thành công");
        return ResponseEntity.ok(response);
    }
}
