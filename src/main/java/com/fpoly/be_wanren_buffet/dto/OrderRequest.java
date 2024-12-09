package com.fpoly.be_wanren_buffet.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private String username;
    private String address;
    private String phone;
    private String email;
    private String notes;
    private String promotion;
    private String payment;
    private Double totalAmount;
    private List<OrderDetailRequest> orderDetails;
}
