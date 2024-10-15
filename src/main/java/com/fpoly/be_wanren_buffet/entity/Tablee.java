package com.fpoly.be_wanren_buffet.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fpoly.be_wanren_buffet.enums.TableStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "[table]")
@Getter
@Setter
public class Tablee extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id")
    private Long tableId;

    @Column(name = "table_number")
    private Integer tableNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "table_status")
    private TableStatus tableStatus;

    @Column(name = "location")
    private String location;

    @JsonManagedReference
    @OneToMany(mappedBy = "tablee")
    private Set<Reservation> reservations;

    @JsonManagedReference
    @OneToMany(mappedBy = "tablee")
    private Set<Order> orders;

}
