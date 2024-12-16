package com.fpoly.be_wanren_buffet.rest.cashier;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.be_wanren_buffet.dao.VoucherRepository;
import com.fpoly.be_wanren_buffet.dto.VoucherDTO;
import com.fpoly.be_wanren_buffet.dto.response.VoucherResponseDTO;
import com.fpoly.be_wanren_buffet.entity.Voucher;

@RestController
@RequestMapping("/Voucher")
public class VoucherRestForCashierController {

    @Autowired
    private VoucherRepository voucherRepository;

    @GetMapping("/{code}/findByCode")
    public ResponseEntity<?> getVoucherByCode(@PathVariable("code") String code) {

        Optional<Voucher> voucher = voucherRepository.findByCode(code);
        VoucherResponseDTO voucherDTO = new VoucherResponseDTO();
        if (voucher.isPresent()) {
            voucherDTO.setVoucherId(voucher.get().getVoucherId());
            voucherDTO.setCustomerId(voucher.get().getCustomer().getCustomerId());
            voucherDTO.setPromotionId(voucher.get().getPromotion().getPromotion());
            voucherDTO.setStatus(voucher.get().getStatus());
            return ResponseEntity.ok(voucherDTO); // Trả về toàn bộ đối tượng Voucher dưới dạng JSON
        } else {
            return ResponseEntity.ok(voucherDTO);
        }
    }

    @GetMapping("/{code}/findByCode2")
    public ResponseEntity<?> getVoucherByCode2(@PathVariable("code") String code) {

        Optional<Voucher> voucher = voucherRepository.findByCode(code);
        VoucherDTO voucherDTO = new VoucherDTO();
        if (voucher.isPresent()) {
            voucherDTO.setVoucherId(voucher.get().getVoucherId());
            voucherDTO.setCustomerId(voucher.get().getCustomer().getCustomerId());
            voucherDTO.setPromotionId(voucher.get().getPromotion().getPromotion());
            voucherDTO.setStatus(voucher.get().getStatus());
            voucherDTO.setDescription(voucher.get().getPromotion().getDescription());
            voucherDTO.setPromotion_value(voucher.get().getPromotion().getPromotionValue());
            voucherDTO.setPromotion_name(voucher.get().getPromotion().getPromotionName());
            return ResponseEntity.ok(voucherDTO); // Trả về toàn bộ đối tượng Voucher dưới dạng JSON
        } else {
            return ResponseEntity.ok(voucherDTO);
        }
    }

}