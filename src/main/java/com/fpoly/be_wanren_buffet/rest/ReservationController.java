package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dto.request.ReservationRequest;
import com.fpoly.be_wanren_buffet.entity.Reservation;
import com.fpoly.be_wanren_buffet.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reservation")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ReservationRequest reservationRequest) {
        Long reservationId = reservationService.createReservation(reservationRequest);
        Map<String, Object> response = new HashMap<>();
        response.put("reservation_id", reservationId);
        response.put("message", "Reservation created successfully");
        return ResponseEntity.ok(response);
    };
}
