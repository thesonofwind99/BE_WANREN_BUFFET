package com.fpoly.be_wanren_buffet.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailForStaffResponse {
    private String productName;
    private Integer quantity;
    private Double price;
}
