package com.fpoly.be_wanren_buffet.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Entity
@Table(name = "user_roles")
@Data
class UserRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
