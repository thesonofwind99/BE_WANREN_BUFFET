// src/main/java/com/fpoly/be_wanren_buffet/dto/OtpValidationRequest.java

package com.fpoly.be_wanren_buffet.dto;

import lombok.Data;

@Data
public class OtpValidationRequest {
    private String email;
    private String otp;


}
