// src/main/java/com/fpoly/be_wanren_buffet/controller/ResetPasswordController.java

package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dto.ResetPasswordRequest;
import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // Sử dụng PasswordEncoder để mã hóa mật khẩu
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/reset-password")
public class ResetPasswordController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Sử dụng PasswordEncoder bean

    /**
     * Đặt lại mật khẩu
     */
    @PostMapping("/update")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        String email = request.getEmail();
        String newPassword = request.getNewPassword();

        Optional<Customer> customerOpt = customerService.findByEmail(email);
        if (!customerOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Email không tồn tại.");
        }

        Customer customer = customerOpt.get();
        customer.setPassword(passwordEncoder.encode(newPassword)); // Mã hóa mật khẩu mới
        customerService.save(customer); // Lưu thông tin khách hàng

        return ResponseEntity.ok("Mật khẩu đã được cập nhật thành công.");
    }
}
