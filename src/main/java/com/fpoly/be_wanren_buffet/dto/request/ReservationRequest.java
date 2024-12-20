package com.fpoly.be_wanren_buffet.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationRequest {
    private Long customerId;
    private Integer numberPeople;
    private Date dateToCome;
    private LocalTime timeToCome;
    private String phoneNumber;
    private String email;
    private String fullName;
    private String note;
}
