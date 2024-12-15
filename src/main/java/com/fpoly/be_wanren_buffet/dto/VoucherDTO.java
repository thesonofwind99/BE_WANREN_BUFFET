package com.fpoly.be_wanren_buffet.dto;

import lombok.Data;

@Data
public class VoucherDTO {
    private Boolean status;
    private Long promotionId;
    private Long customerId;
}
