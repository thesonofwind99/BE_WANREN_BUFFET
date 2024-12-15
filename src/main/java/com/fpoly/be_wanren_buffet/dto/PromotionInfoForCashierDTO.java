package com.fpoly.be_wanren_buffet.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PromotionInfoForCashierDTO {
    private String promotionName;
    private Double promotionValue;
    private LocalDateTime promotionEndDate;

}