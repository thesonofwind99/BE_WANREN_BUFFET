package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.PromotionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "Promotion_order")
public interface PromotionOrderRepository extends JpaRepository<PromotionOrder, Long> {
}
