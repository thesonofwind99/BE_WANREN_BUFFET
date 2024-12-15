package com.fpoly.be_wanren_buffet.rest.cashier;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.be_wanren_buffet.dao.PromotionOrderRepository;
import com.fpoly.be_wanren_buffet.dao.PromotionRepository;
import com.fpoly.be_wanren_buffet.dto.PromotionInfoForCashierDTO;
import com.fpoly.be_wanren_buffet.dto.PromotionOrderDTO;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.entity.Promotion;
import com.fpoly.be_wanren_buffet.entity.PromotionOrder;
import com.fpoly.be_wanren_buffet.service.OrderForStaffService;

@RestController
@RequestMapping("/Promotion")
public class PromotionRestForCashierController {
    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private PromotionOrderRepository promotionOrderRepository;

    @Autowired
    private OrderForStaffService orderForStaffService;

    @GetMapping("/{id}/info")
    public ResponseEntity<PromotionInfoForCashierDTO> getPromotionInfoById(@PathVariable("id") Long promotionId) {
        Optional<Promotion> promotionOptional = promotionRepository.findById(promotionId);

        if (promotionOptional.isPresent()) {
            Promotion promotion = promotionOptional.get();

            // Ánh xạ từ thực thể Promotion sang DTO
            PromotionInfoForCashierDTO dto = new PromotionInfoForCashierDTO(
                    promotion.getPromotionName(),
                    promotion.getPromotionValue(), promotion.getEndDate());

            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/createPromotionOrder")
    public ResponseEntity<PromotionOrder> createPromotionOrder(@RequestBody PromotionOrderDTO promotionOrderDTO) {
        // Kiểm tra xem Promotion có tồn tại hay không
        Optional<Promotion> promotionOptional = promotionRepository.findById(promotionOrderDTO.getPromotionId());

        if (promotionOptional.isPresent()) {
            Promotion promotion = promotionOptional.get();
            Order order = orderForStaffService.findOrderById(promotionOrderDTO.getOrderId());
            // Tạo PromotionOrder từ dữ liệu DTO
            PromotionOrder promotionOrder = new PromotionOrder();
            promotionOrder.setPromotion(promotion); // Set Promotion cho PromotionOrder
            promotionOrder.setOrder(order); // Set OrderId
            // Nếu cần thêm các trường khác như số lượng hoặc chiết khấu, bạn có thể thêm
            // vào DTO và set vào PromotionOrder

            // Lưu PromotionOrder vào cơ sở dữ liệu
            PromotionOrder savedPromotionOrder = promotionOrderRepository.save(promotionOrder);

            return ResponseEntity.ok(savedPromotionOrder);
        }

        return ResponseEntity.notFound().build(); // Nếu không tìm thấy Promotion
    }

}