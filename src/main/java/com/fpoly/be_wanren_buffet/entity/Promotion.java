package com.fpoly.be_wanren_buffet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "promotion")
@Getter
@Setter
public class Promotion extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_id")
    private Long promotionId;

    @Column(name = "promotion_name")
    private String promotionName;

    @Column(name = "description")
    private String description;

    @Column(name = "promotion_type")
    private String promotionType;

    @Column(name = "promotion_value")
    private Double promotionValue;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "promotion_status")
    private Boolean promotionStatus; //True: con han, False: het han

    @OneToMany(mappedBy = "promotion")
    private Set<PromotionOrder> promotionOrders;

}
