package com.fpoly.be_wanren_buffet.security;

import com.fpoly.be_wanren_buffet.dto.UpdateCustomerDTO;

public class JwtResponse {
    private String token;
    private UpdateCustomerDTO userInfo;

    public JwtResponse(String token) {
        this.token = token;
    }

    public JwtResponse(String token, UpdateCustomerDTO userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UpdateCustomerDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UpdateCustomerDTO userInfo) {
        this.userInfo = userInfo;
    }
}
