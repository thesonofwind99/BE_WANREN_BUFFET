package com.fpoly.be_wanren_buffet.dto;

import com.fpoly.be_wanren_buffet.enums.ReservationStatus;

import java.time.LocalTime;
import java.util.Date;

public class ReservationStaffDTO {
    private Long reservationId;
    private Long userId;
    private Long customerId;
    private Integer numberPeople;
    private Date dateToCome;
    private LocalTime timeToCome;
    private String phoneNumber;
    private String email;
    private String fullName;
    private String note;
    private ReservationStatus status;

    public ReservationStaffDTO(Long reservationId, Long userId, Long customerId, Integer numberPeople, Date dateToCome,
                               LocalTime timeToCome, String phoneNumber, String email, String fullName,
                               String note, ReservationStatus status) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.customerId = customerId;
        this.numberPeople = numberPeople;
        this.dateToCome = dateToCome;
        this.timeToCome = timeToCome;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.fullName = fullName;
        this.note = note;
        this.status = status;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getNumberPeople() {
        return numberPeople;
    }

    public void setNumberPeople(Integer numberPeople) {
        this.numberPeople = numberPeople;
    }

    public Date getDateToCome() {
        return dateToCome;
    }

    public void setDateToCome(Date dateToCome) {
        this.dateToCome = dateToCome;
    }

    public LocalTime getTimeToCome() {
        return timeToCome;
    }

    public void setTimeToCome(LocalTime timeToCome) {
        this.timeToCome = timeToCome;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
