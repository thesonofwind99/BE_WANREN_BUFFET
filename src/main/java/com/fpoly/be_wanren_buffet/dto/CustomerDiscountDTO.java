package com.fpoly.be_wanren_buffet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDiscountDTO {
    private Long customerId;
    private Long discountPointUsed;

    public CustomerDiscountDTO() {

    }
}