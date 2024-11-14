// src/main/java/com/fpoly/be_wanren_buffet/service/OtpService.java

package com.fpoly.be_wanren_buffet.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    // Lưu trữ OTP tạm thời trong bộ nhớ (HashMap). Trong thực tế, bạn nên sử dụng cơ sở dữ liệu hoặc cache như Redis
    private Map<String, OtpEntry> otpStorage = new HashMap<>();

    // Thời gian hết hạn OTP (phút)
    public static final int OTP_EXPIRATION_MINUTES = 10;

    /**
     * Tạo OTP ngẫu nhiên 6 chữ số
     *
     * @param email Email của người dùng để liên kết với OTP
     * @return Mã OTP được tạo
     */
    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES);
        otpStorage.put(email, new OtpEntry(otp, expirationTime));
        return otp;
    }

    /**
     * Xác thực OTP
     *
     * @param email Email của người dùng
     * @param otp   OTP mà người dùng nhập vào
     * @return True nếu OTP hợp lệ, ngược lại False
     */
    public boolean validateOtp(String email, String otp) {
        if (!otpStorage.containsKey(email)) {
            return false;
        }

        OtpEntry otpEntry = otpStorage.get(email);
        if (otpEntry.getExpirationTime().isBefore(LocalDateTime.now())) {
            otpStorage.remove(email);
            return false;
        }

        if (otpEntry.getOtp().equals(otp)) {
            otpStorage.remove(email);
            return true;
        }

        return false;
    }

    /**
     * Lớp lưu trữ thông tin OTP
     */
    private static class OtpEntry {
        private String otp;
        private LocalDateTime expirationTime;

        public OtpEntry(String otp, LocalDateTime expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpirationTime() {
            return expirationTime;
        }
    }
}
