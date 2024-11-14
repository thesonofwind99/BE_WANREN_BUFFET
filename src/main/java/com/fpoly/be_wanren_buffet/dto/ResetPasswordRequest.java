// src/main/java/com/fpoly/be_wanren_buffet/dto/ResetPasswordRequest.java

package com.fpoly.be_wanren_buffet.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String newPassword;

}
