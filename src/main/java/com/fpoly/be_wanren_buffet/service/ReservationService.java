package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dto.ReservationStaffDTO;
import com.fpoly.be_wanren_buffet.dto.request.ReservationRequest;

import java.util.List;

public interface ReservationService {
    Long createReservation(ReservationRequest reservationRequest);

    List<ReservationStaffDTO> getReservationsForToday();
}
