// src/main/java/com/fpoly/be_wanren_buffet/controller/ForgotPasswordController.java

package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dto.ForgotPasswordRequest;
import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.service.CustomerService;
import com.fpoly.be_wanren_buffet.service.EmailService;
import com.fpoly.be_wanren_buffet.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/forgot-password")// Cấu hình CORS (nếu frontend và backend chạy trên các domain/port khác nhau)
public class ForgotPasswordController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    /**
     * Yêu cầu đặt lại mật khẩu bằng email
     */
    @PostMapping("/request")
    public ResponseEntity<?> requestPasswordReset(@RequestBody ForgotPasswordRequest request) {
        String email = request.getEmail();
        Optional<Customer> customerOpt = customerService.findByEmail(email);
        if (!customerOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Email không tồn tại.");
        }

        String otp = otpService.generateOtp(email);
        String subject = "Yêu cầu đặt lại mật khẩu";
        String body = "Mã OTP của bạn là: " + otp + "\n\n" +
                "Mã OTP này có hiệu lực trong " + OtpService.OTP_EXPIRATION_MINUTES + " phút.";

        emailService.sendSimpleEmail(email, subject, body);

        return ResponseEntity.ok("Đã gửi mã OTP tới email của bạn.");
    }
}
