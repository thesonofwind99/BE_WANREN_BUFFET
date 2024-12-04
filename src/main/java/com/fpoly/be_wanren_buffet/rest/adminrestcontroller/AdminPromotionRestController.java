package com.fpoly.be_wanren_buffet.rest.adminrestcontroller;

import com.fpoly.be_wanren_buffet.dao.PromotionRepository;
import com.fpoly.be_wanren_buffet.entity.Promotion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/Promotion")
@RequiredArgsConstructor
public class AdminPromotionRestController {

    private final PromotionRepository promotionRepository;

    @PostMapping("/create")
    public ResponseEntity<Promotion> createPromotion(@RequestBody Promotion promotion) {
        try {
            return new ResponseEntity<>(promotionRepository.save(promotion), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating promotion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<Promotion> updatePromotion(
            @PathVariable("id") Long id,
            @RequestBody Promotion promotionUpdates) {

        Optional<Promotion> optionalPromotion = promotionRepository.findById(id);
        if (optionalPromotion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Promotion existingPromotion = optionalPromotion.get();

        // Cập nhật từng trường chỉ nếu trường đó không null
        if (promotionUpdates.getPromotionName() != null) {
            existingPromotion.setPromotionName(promotionUpdates.getPromotionName());
        }
        if (promotionUpdates.getDescription() != null) {
            existingPromotion.setDescription(promotionUpdates.getDescription());
        }
        if (promotionUpdates.getPromotionType() != null) {
            existingPromotion.setPromotionType(promotionUpdates.getPromotionType());
        }
        if (promotionUpdates.getPromotionValue() != null) {
            existingPromotion.setPromotionValue(promotionUpdates.getPromotionValue());
        }
        if (promotionUpdates.getStartDate() != null) {
            existingPromotion.setStartDate(promotionUpdates.getStartDate());
        }
        if (promotionUpdates.getEndDate() != null) {
            existingPromotion.setEndDate(promotionUpdates.getEndDate());
        }
        if (promotionUpdates.getPromotionStatus() != null) {
            existingPromotion.setPromotionStatus(promotionUpdates.getPromotionStatus());
        }

        // Lưu đối tượng đã cập nhật
        try {
            return new ResponseEntity<>(promotionRepository.save(existingPromotion), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating promotion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable("id") Long id) {
        try {
            if (promotionRepository.existsById(id)) {
                promotionRepository.deleteById(id);
                return ResponseEntity.noContent().build(); // 204 No Content
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
            }
        } catch (Exception e) {
            log.error("Error deleting promotion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
