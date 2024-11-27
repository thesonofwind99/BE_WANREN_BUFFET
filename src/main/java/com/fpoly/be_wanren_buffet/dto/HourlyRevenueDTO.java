package com.fpoly.be_wanren_buffet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class HourlyRevenueDTO {
    private Integer hourOfDay;
    private Double hourlyRevenue;
}
