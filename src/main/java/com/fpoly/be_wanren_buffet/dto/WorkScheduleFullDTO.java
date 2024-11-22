package com.fpoly.be_wanren_buffet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkScheduleFullDTO {
    private String username;
    private String fullName;
    private String userType;
    private Long shiftId;
    private Date workDate;
}
