package com.fpoly.be_wanren_buffet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PromotionOrderDTO {
    private Long orderId;
    private Long promotionId;
}