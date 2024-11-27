package com.fpoly.be_wanren_buffet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeeklyRevenueDTO {
    private String dayOfWeek;
    private Double dailyRevenue;
}
