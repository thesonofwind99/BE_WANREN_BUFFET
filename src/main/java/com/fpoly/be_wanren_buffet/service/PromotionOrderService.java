package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dao.PromotionOrderRepository;
import com.fpoly.be_wanren_buffet.dto.PromotionInfoDTO;
import org.springframework.stereotype.Service;

@Service
public class PromotionOrderService {
    private final PromotionOrderRepository promotionOrderRepository;

    public PromotionOrderService(PromotionOrderRepository promotionOrderRepository) {
        this.promotionOrderRepository = promotionOrderRepository;
    }

    public PromotionInfoDTO getPromotionInfoByOrderId(Long orderId) {
        return promotionOrderRepository.findPromotionInfoByOrderId(orderId);
    }
}
