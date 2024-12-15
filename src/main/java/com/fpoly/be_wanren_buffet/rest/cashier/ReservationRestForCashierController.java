package com.fpoly.be_wanren_buffet.rest.cashier;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.be_wanren_buffet.dao.ReservationRepository;
import com.fpoly.be_wanren_buffet.dto.ReservationDTO;
import com.fpoly.be_wanren_buffet.entity.Reservation;
import com.fpoly.be_wanren_buffet.enums.ReservationStatus;
import com.fpoly.be_wanren_buffet.service.ReservationServiceForCashier;

@RestController
@RequestMapping("/Reservation")
public class ReservationRestForCashierController {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationServiceForCashier reservationService;

    @GetMapping("/all")
    public List<ReservationDTO> getAllTables() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservation -> new ReservationDTO(reservation.getReservationId(),
                        reservation.getCreatedDate(),
                        reservation.getUpdatedDate(),
                        reservation.getNumberPeople(),
                        (Date) reservation.getDateToCome(),
                        reservation.getTimeToCome(),
                        reservation.getPhoneNumber(),
                        reservation.getEmail(),
                        reservation.getFullName(),
                        reservation.getNote(),
                        reservation.getStatus()))
                .collect(Collectors.toList());
    }

    @PostMapping("/createNewReservation")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        // Ánh xạ DTO sang Entity
        Reservation reservation = new Reservation();
        reservation.setNumberPeople(reservationDTO.getNumberPeople());
        reservation.setDateToCome(reservationDTO.getDateToCome());
        reservation.setTimeToCome(reservationDTO.getTimeToCome());
        reservation.setPhoneNumber(reservationDTO.getPhoneNumber());
        reservation.setEmail(reservationDTO.getEmail());
        reservation.setFullName(reservationDTO.getFullName());
        reservation.setNote(reservationDTO.getNote());
        reservation.setStatus(ReservationStatus.APPROVED); // Đặt trạng thái mặc định là "PENDING"

        // Lưu vào cơ sở dữ liệu (Giả sử bạn sử dụng một service để lưu)
        Reservation savedReservation = reservationService.saveReservation(reservation);

        // Chuyển đổi đối tượng Entity sang DTO để trả về
        ReservationDTO response = new ReservationDTO(
                savedReservation.getReservationId(), savedReservation.getCreatedDate(),
                savedReservation.getUpdatedDate(), savedReservation.getNumberPeople(), savedReservation.getDateToCome(),
                savedReservation.getTimeToCome(), savedReservation.getPhoneNumber(), savedReservation.getEmail(),
                savedReservation.getFullName(), savedReservation.getNote(), savedReservation.getStatus());

        // Trả về ResponseEntity với mã trạng thái CREATED (201)
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
