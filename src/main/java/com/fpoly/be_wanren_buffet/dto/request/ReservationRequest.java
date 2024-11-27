package com.fpoly.be_wanren_buffet.dto.request;

import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequest {
    private Long customerId;
    private LocalDateTime dateToCome;
    private String phoneNumber;
    private String fullName;
    private String note;
}