package com.fpoly.be_wanren_buffet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkShiftDTO {
    private Long userId;
    private String username;
    private String fullName;
    private String position;
    private Long totalHours;
}
