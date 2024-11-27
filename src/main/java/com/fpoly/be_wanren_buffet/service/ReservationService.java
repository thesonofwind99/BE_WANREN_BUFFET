package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dto.request.ReservationRequest;

public interface ReservationService {
    Long createReservation(ReservationRequest reservationRequest);
}
