package com.fpoly.be_wanren_buffet.dto;

import lombok.Data;

@Data
public class OrderDetailRequest {
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private String itemNotes;
}
