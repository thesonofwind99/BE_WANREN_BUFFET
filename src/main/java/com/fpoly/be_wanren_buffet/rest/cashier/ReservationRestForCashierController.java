package com.fpoly.be_wanren_buffet.rest.cashier;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.be_wanren_buffet.dao.ReservationRepository;
import com.fpoly.be_wanren_buffet.dto.ReservationDTO;
import com.fpoly.be_wanren_buffet.entity.Reservation;

@RestController
@RequestMapping("/Reservation")
public class ReservationRestForCashierController {
    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/all")
    public List<ReservationDTO> getAllTables() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservation -> new ReservationDTO(reservation.getReservationId()))
                .collect(Collectors.toList());
    }
}
