package com.fpoly.be_wanren_buffet.service.Impl;

import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.dao.ReservationRepository;
import com.fpoly.be_wanren_buffet.dto.request.ReservationRequest;
import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.entity.Reservation;
import com.fpoly.be_wanren_buffet.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Long createReservation(ReservationRequest reservationRequest) {
        Customer customer = customerRepository.findById(reservationRequest.getCustomerId()).get();

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setDateToCome(reservationRequest.getDateToCome());
        reservation.setFullName(reservationRequest.getFullName());
        reservation.setPhoneNumber(reservationRequest.getPhoneNumber());
        reservation.setNote(reservationRequest.getNote());
        reservation.setCreatedDate(LocalDateTime.now());
        Reservation savedReservation = reservationRepository.save(reservation);
        return savedReservation.getReservationId();
    }
}
