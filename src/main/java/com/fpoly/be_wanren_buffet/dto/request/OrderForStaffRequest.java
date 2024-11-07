package com.fpoly.be_wanren_buffet.dto.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderForStaffRequest {
    private Long userId;
    private Long customerId;
    private String address;
    private String notes;
    private String orderStatus;
    private Double totalAmount;
    private Long tableId;
}
