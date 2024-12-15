package com.fpoly.be_wanren_buffet.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PromotionVoucherDTO {
    private String promotionName;
    private String image;
    private LocalDateTime endDate;
    private String voucherCode;
    private Boolean voucherStatus;

    public PromotionVoucherDTO(String promotionName, String image, LocalDateTime endDate, String voucherCode, Boolean voucherStatus) {
        this.promotionName = promotionName;
        this.image = image;
        this.endDate = endDate;
        this.voucherCode = voucherCode;
        this.voucherStatus = voucherStatus;
    }

}

