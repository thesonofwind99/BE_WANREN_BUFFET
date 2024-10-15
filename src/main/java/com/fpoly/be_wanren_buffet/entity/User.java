package com.fpoly.be_wanren_buffet.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "[user]")
@Getter
@Setter
public class User extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

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

    @Column(name = "user_type")
    private String userType;

    @Column(name = "account_status")
    private Boolean accountStatus; //True: hoat dong, False: khong hoat dong

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonManagedReference
    private Set<Role> roles;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private Set<WorkSchedule> schedules;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private Set<Reservation> reservations;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private Set<LoginHistory> loginHistories;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private Set<Order> orders;

    @JsonBackReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Payment payment;
}
