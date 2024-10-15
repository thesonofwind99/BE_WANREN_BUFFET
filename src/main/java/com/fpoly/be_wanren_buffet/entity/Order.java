package com.fpoly.be_wanren_buffet.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fpoly.be_wanren_buffet.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "[order]")
@Getter
@Setter
public class Order extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "notes")
    private String notes;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "table_id")
    private Tablee tablee;

    @JsonBackReference
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @JsonBackReference
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Review review;

    @JsonManagedReference
    @OneToMany(mappedBy = "order")
    private Set<PromotionOrder> promotionOrders;

    @JsonManagedReference
    @OneToMany(mappedBy = "order")
    private Set<OrderDetail> orderDetails;

}
