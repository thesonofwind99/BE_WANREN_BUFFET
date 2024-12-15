package com.fpoly.be_wanren_buffet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.be_wanren_buffet.dao.ReservationRepository;
import com.fpoly.be_wanren_buffet.entity.Reservation;

@Service
public class ReservationServiceForCashier {
    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation); // Lưu đối tượng vào cơ sở dữ liệu
    }
}
