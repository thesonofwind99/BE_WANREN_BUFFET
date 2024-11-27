package com.fpoly.be_wanren_buffet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@Setter
public class Reservation extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private Tablee tablee;

    @JoinColumn(name = "date_to_come")
    private LocalDateTime dateToCome;

    @JoinColumn(name = "phone_number")
    private String phoneNumber;

    @JoinColumn(name = "full_name")
    private String fullName;

    @JoinColumn(name = "note")
    private String note;

}
