package com.fpoly.be_wanren_buffet.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "customer")
@Getter
@Setter
public class Customer extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "loyalty_points")
    private Long loyaltyPoints;

    @Column(name = "customer_type")
    private String customerType;

    @Column(name = "account_status")
    private Boolean accountStatus; //True: hoat dong, False: khong hoat dong

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private Set<Reservation> reservations;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private Set<Order> orders;
}
