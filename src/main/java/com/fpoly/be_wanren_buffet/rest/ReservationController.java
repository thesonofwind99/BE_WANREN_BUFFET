package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dao.ReservationRepository;
import com.fpoly.be_wanren_buffet.dto.ReservationStaffDTO;
import com.fpoly.be_wanren_buffet.dto.request.ReservationRequest;
import com.fpoly.be_wanren_buffet.entity.Reservation;
import com.fpoly.be_wanren_buffet.enums.ReservationStatus;
import com.fpoly.be_wanren_buffet.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservation")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ReservationRequest reservationRequest) {
        Long reservationId = reservationService.createReservation(reservationRequest);
        Map<String, Object> response = new HashMap<>();
        response.put("reservation_id", reservationId);
        response.put("message", "Reservation created successfully");
        return ResponseEntity.ok(response);
    };

    @GetMapping("/today")
    public List<ReservationStaffDTO> getReservationsForToday() {
        return reservationService.getReservationsForToday();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateReservationStatus(@PathVariable("id") Long reservationId,
                                                          @RequestParam("status") ReservationStatus status) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setStatus(status);
            reservationRepository.save(reservation);
        }

        // Return success message regardless of whether the reservation was found or not
        return ResponseEntity.ok("Reservation status updated successfully.");
    }
}
