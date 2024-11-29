package com.fpoly.be_wanren_buffet.dto;

public class UpdateLoyaltyPointsRequest {
    private String phoneNumber;
    private Long pointsToDeduct;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getPointsToDeduct() {
        return pointsToDeduct;
    }

    public void setPointsToDeduct(Long pointsToDeduct) {
        this.pointsToDeduct = pointsToDeduct;
    }
}
