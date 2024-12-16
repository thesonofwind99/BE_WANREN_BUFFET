package com.fpoly.be_wanren_buffet.rest.cashier;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.be_wanren_buffet.dao.ReservationRepository;
import com.fpoly.be_wanren_buffet.dao.UserRepository;
import com.fpoly.be_wanren_buffet.dto.ReservationDTO;
import com.fpoly.be_wanren_buffet.entity.Reservation;
import com.fpoly.be_wanren_buffet.entity.User;
import com.fpoly.be_wanren_buffet.enums.ReservationStatus;
import com.fpoly.be_wanren_buffet.service.ReservationServiceForCashier;

@RestController
@RequestMapping("/Reservation")
public class ReservationRestForCashierController {
        @Autowired
        private ReservationRepository reservationRepository;

        @Autowired
        private ReservationServiceForCashier reservationService;

        @Autowired
        private UserRepository userRepository;

        @GetMapping("/all")
        public List<ReservationDTO> getAllTables(
                        @RequestParam(value = "limit", required = false) Integer limit) {
                List<Reservation> reservations = reservationRepository.findAll()
                                .stream()
                                .sorted(Comparator.comparing(Reservation::getUpdatedDate))
                                .collect(Collectors.toList());

                if (limit != null && limit > 0) {
                        reservations = reservations.stream()
                                        .filter(reservation -> reservation.getUpdatedDate() != null)
                                        .sorted(Comparator.comparing(Reservation::getUpdatedDate).reversed())
                                        .limit(limit)
                                        .collect(Collectors.toList());
                }

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
                                savedReservation.getUpdatedDate(), savedReservation.getNumberPeople(),
                                savedReservation.getDateToCome(),
                                savedReservation.getTimeToCome(), savedReservation.getPhoneNumber(),
                                savedReservation.getEmail(),
                                savedReservation.getFullName(), savedReservation.getNote(),
                                savedReservation.getStatus());

                // Trả về ResponseEntity với mã trạng thái CREATED (201)
                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        @PutMapping("/{reservationId}/user")
        public ResponseEntity<?> updateUserForReservation(
                        @PathVariable Long reservationId,
                        @RequestBody Map<String, Object> payload) {
                Long userId = Long.valueOf(payload.get("userId").toString());
                Reservation reservation = reservationRepository.findById(reservationId)
                                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                reservation.setUser(user); // Cập nhật userId cho reservation
                reservationRepository.save(reservation);

                return ResponseEntity.ok("User updated successfully");
        }

}
