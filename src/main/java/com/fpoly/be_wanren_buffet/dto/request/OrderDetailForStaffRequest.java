package com.fpoly.be_wanren_buffet.dto.request;

import com.fpoly.be_wanren_buffet.entity.OrderDetail;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDetailForStaffRequest {
    private Long orderDetailId;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private String itemNotes;
    private Long orderId;
    private LocalDateTime createdDate;

}
