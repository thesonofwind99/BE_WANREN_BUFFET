package com.fpoly.be_wanren_buffet.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentForStaffRequest {
    private Double amountPaid;
    private String paymentMethod;
    private Boolean paymentStatus;
    private Long orderId;
    private Long userId;
}
