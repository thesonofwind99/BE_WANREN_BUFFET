package com.fpoly.be_wanren_buffet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "promotion_order", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"promotion_id", "order_id"})
})
@Getter
@Setter
public class PromotionOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_order_id")
    private Long promotionOrderId;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
