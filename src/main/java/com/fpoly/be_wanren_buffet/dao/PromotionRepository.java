package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "Promotion")
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
}
