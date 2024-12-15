package com.fpoly.be_wanren_buffet.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import com.fpoly.be_wanren_buffet.enums.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private Long reservationId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private int numberPeople;
    private Date dateToCome;
    private LocalTime timeToCome;
    private String phoneNumber;
    private String email;
    private String fullName;
    private String note;
    private ReservationStatus status;
}
