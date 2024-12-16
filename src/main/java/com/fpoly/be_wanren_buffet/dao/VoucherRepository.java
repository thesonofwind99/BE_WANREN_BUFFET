package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.dto.PromotionVoucherDTO;
import com.fpoly.be_wanren_buffet.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "Voucher")
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Optional<Voucher> findByCode(String code);
    @Query("SELECT new com.fpoly.be_wanren_buffet.dto.PromotionVoucherDTO(p.promotionName, p.image, p.endDate, v.code, v.status, v.customer.customerId, p.Promotion) " +
            "FROM voucher v " +
            "JOIN v.promotion p " +
            "WHERE v.customer.customerId = :customerId")
    List<PromotionVoucherDTO> findPromotionVoucherInfoByCustomerId(@Param("customerId") Long customerId);
}

