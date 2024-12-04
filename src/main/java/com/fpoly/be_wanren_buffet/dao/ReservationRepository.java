package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.dto.ReservationStaffDTO;
import com.fpoly.be_wanren_buffet.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(path = "Reservation")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT new com.fpoly.be_wanren_buffet.dto.ReservationStaffDTO(" +
            "r.reservationId ,r.user.userId, r.customer.customerId, r.numberPeople, r.dateToCome, " +
            "r.timeToCome, r.phoneNumber, r.email, r.fullName, r.note, r.status) " +
            "FROM Reservation r WHERE FUNCTION('DATE', r.dateToCome) = :today")
    List<ReservationStaffDTO> findReservationsByToday(LocalDate today);
}
