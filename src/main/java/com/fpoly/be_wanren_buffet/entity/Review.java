package com.fpoly.be_wanren_buffet.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "review")
@Getter
@Setter
public class Review extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "content")
    private String content;

    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
