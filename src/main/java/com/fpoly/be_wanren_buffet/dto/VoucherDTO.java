package com.fpoly.be_wanren_buffet.dto;

import lombok.Data;

@Data
public class VoucherDTO {
    private Long voucherId;
    private Boolean status;
    private Long promotionId;
    private Long customerId;
    private String description;
    private String promotion_name;
    private Double promotion_value;
}
