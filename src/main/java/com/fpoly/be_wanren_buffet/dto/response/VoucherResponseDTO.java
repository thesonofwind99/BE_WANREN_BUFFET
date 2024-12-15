package com.fpoly.be_wanren_buffet.dto.response;

import lombok.Data;

@Data
public class VoucherResponseDTO {
    private Long voucherId;
    private String code;
    private Boolean status;
    private Long promotionId;
    private Long customerId;
}