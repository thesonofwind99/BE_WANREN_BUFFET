package com.fpoly.be_wanren_buffet.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Table
@Entity(name = "voucher")
public class Voucher extends Auditable implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Long voucherId;

    @JoinColumn(name = "code")
    private String code;

    @JoinColumn(name = "status")
    private Boolean status; //true da dung, false chua dung

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
