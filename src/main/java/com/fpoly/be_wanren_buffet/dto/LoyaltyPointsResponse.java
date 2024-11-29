package com.fpoly.be_wanren_buffet.dto;

public class LoyaltyPointsResponse {
    private String phoneNumber;
    private Long loyaltyPoints;

    public LoyaltyPointsResponse(String phoneNumber, Long loyaltyPoints) {
        this.phoneNumber = phoneNumber;
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Long loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
}
