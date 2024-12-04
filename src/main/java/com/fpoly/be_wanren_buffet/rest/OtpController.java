// src/main/java/com/fpoly/be_wanren_buffet/controller/OtpController.java

package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dto.OtpValidationRequest;
import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.service.CustomerService;
import com.fpoly.be_wanren_buffet.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/otp")// Điều chỉnh nếu frontend chạy trên domain/port khác
public class OtpController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OtpService otpService;

    /**
     * Xác thực OTP
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateOtp(@RequestBody OtpValidationRequest request) {
        String email = request.getEmail();
        String otp = request.getOtp();

        Optional<Customer> customerOpt = customerService.findByEmail(email);
        if (!customerOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Email không tồn tại.");
        }

        boolean isValid = otpService.validateOtp(email, otp);
        if (isValid) {
            return ResponseEntity.ok("OTP hợp lệ.");
        } else {
            return ResponseEntity.badRequest().body("OTP không hợp lệ hoặc đã hết hạn.");
        }
    }
}
