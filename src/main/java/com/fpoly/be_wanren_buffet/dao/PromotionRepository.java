package com.fpoly.be_wanren_buffet.dao;

import org.springframework.data.domain.Page;
import com.fpoly.be_wanren_buffet.entity.Promotion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "Promotion")
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    @Query("SELECT p FROM Promotion p WHERE p.promotionStatus = :promotionStatus AND p.endDate > CURRENT_DATE")
    Page<Promotion> findByPromotionStatus(@Param("promotionStatus") Boolean promotionStatus, Pageable pageable);

    Page<Promotion> findByPromotionNameContaining(String promotionName, Pageable pageable);

}
