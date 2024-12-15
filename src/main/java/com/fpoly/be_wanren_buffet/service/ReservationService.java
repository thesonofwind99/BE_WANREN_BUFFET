package com.fpoly.be_wanren_buffet.service;

import java.util.List;

import com.fpoly.be_wanren_buffet.dto.ReservationStaffDTO;
import com.fpoly.be_wanren_buffet.dto.request.ReservationRequest;

public interface ReservationService {
    Long createReservation(ReservationRequest reservationRequest);

    List<ReservationStaffDTO> getReservationsForToday();
}
