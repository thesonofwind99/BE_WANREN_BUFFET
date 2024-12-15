package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dto.PromotionVoucherDTO;
import com.fpoly.be_wanren_buffet.dto.VoucherDTO;
import com.fpoly.be_wanren_buffet.dto.response.VoucherResponseDTO;
import com.fpoly.be_wanren_buffet.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @PostMapping
    public ResponseEntity<VoucherResponseDTO> createVoucher(@RequestBody VoucherDTO voucherDTO) {
        VoucherResponseDTO createdVoucher = voucherService.createVoucher(voucherDTO);
        return ResponseEntity.ok(createdVoucher);
    }

    @GetMapping("/voucherInfo/{customerId}")
    public ResponseEntity<List<PromotionVoucherDTO>> getPromotionVoucherInfoByCustomerId(@PathVariable Long customerId) {
        List<PromotionVoucherDTO> promotionVoucherDTOs = voucherService.getPromotionVoucherInfo(customerId);

        if (promotionVoucherDTOs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(promotionVoucherDTOs);
    }
}

