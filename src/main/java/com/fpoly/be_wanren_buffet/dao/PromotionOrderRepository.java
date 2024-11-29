package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.dto.PromotionInfoDTO;
import com.fpoly.be_wanren_buffet.entity.PromotionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "Promotion_order")
public interface PromotionOrderRepository extends JpaRepository<PromotionOrder, Long> {
    @Query("SELECT new com.fpoly.be_wanren_buffet.dto.PromotionInfoDTO(po.promotion.promotionName, po.promotion.promotionValue) " +
            "FROM PromotionOrder po " +
            "WHERE po.order.orderId = :orderId")
    PromotionInfoDTO findPromotionInfoByOrderId(@Param("orderId") Long orderId);
}
