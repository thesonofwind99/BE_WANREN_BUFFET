package com.fpoly.be_wanren_buffet.dto;

import java.time.LocalDateTime;

import com.fpoly.be_wanren_buffet.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private LocalDateTime createdDate;
    private Long orderId;
    private OrderStatus orderStatus;
    private double totalAmount;
    private String notes;
    private String address;
    private Long discountPointUsed;
    private Integer numberPeople;
    private String phoneNumber; // Số điện thoại của khách hàng
    private String fullName; // Tên đầy đủ của khách hàng
    private Long tableId; // ID của bàn
}
