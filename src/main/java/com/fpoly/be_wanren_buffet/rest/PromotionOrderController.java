package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dto.PromotionInfoDTO;
import com.fpoly.be_wanren_buffet.service.PromotionOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/promotions")
public class PromotionOrderController {
    private final PromotionOrderService promotionOrderService;

    public PromotionOrderController(PromotionOrderService promotionOrderService) {
        this.promotionOrderService = promotionOrderService;
    }

    @GetMapping("/info/{orderId}")
    public ResponseEntity<PromotionInfoDTO> getPromotionInfoByOrderId(@PathVariable Long orderId) {
        PromotionInfoDTO promotionInfo = promotionOrderService.getPromotionInfoByOrderId(orderId);
        if (promotionInfo != null) {
            return ResponseEntity.ok(promotionInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
