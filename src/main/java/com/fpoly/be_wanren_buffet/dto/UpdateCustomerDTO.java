package com.fpoly.be_wanren_buffet.dto;

import lombok.Data;

@Data
public class UpdateCustomerDTO {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;
    private String jwtToken;
}
