package com.fpoly.be_wanren_buffet.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Table
@Entity(name = "voucher")
public class Voucher extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Long voucherId;

    @Column(name = "code") // Đổi từ @JoinColumn thành @Column
    private String code;

    @Column(name = "status") // Đổi từ @JoinColumn thành @Column
    private Boolean status; // true đã dùng, false chưa dùng

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // Getter và Setter cho voucherId
    public Long getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
    }

    // Getter và Setter cho code
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // Getter và Setter cho status
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    // Getter và Setter cho promotion
    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    // Getter và Setter cho customer
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
