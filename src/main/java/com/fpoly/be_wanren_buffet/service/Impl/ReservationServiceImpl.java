package com.fpoly.be_wanren_buffet.service.Impl;

import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.dao.ReservationRepository;
import com.fpoly.be_wanren_buffet.dto.ReservationStaffDTO;
import com.fpoly.be_wanren_buffet.dto.request.ReservationRequest;
import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.entity.Reservation;
import com.fpoly.be_wanren_buffet.enums.ReservationStatus;
import com.fpoly.be_wanren_buffet.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Long createReservation(ReservationRequest reservationRequest) {
        Customer customer;
        System.out.println(reservationRequest.getCustomerId());
        if(reservationRequest.getCustomerId() != 0){
             customer = customerRepository.findById(reservationRequest.getCustomerId()).get();
        }else{
             customer = null;
        }


        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setNumberPeople(reservationRequest.getNumberPeople());
        reservation.setDateToCome(reservationRequest.getDateToCome());
        reservation.setTimeToCome(reservationRequest.getTimeToCome());
        reservation.setFullName(reservationRequest.getFullName());
        reservation.setPhoneNumber(reservationRequest.getPhoneNumber());
        reservation.setEmail(reservationRequest.getEmail());
        reservation.setNote(reservationRequest.getNote());
        reservation.setCreatedDate(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.PENDING);
        Reservation savedReservation = reservationRepository.save(reservation);
        return savedReservation.getReservationId();
    }

    @Override
    public List<ReservationStaffDTO> getReservationsForToday() {
        LocalDate today = LocalDate.now();
        return reservationRepository.findReservationsByToday(today);
    }
}
