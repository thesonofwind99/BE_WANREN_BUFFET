package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.dao.PromotionRepository;
import com.fpoly.be_wanren_buffet.dao.VoucherRepository;
import com.fpoly.be_wanren_buffet.dto.PromotionVoucherDTO;
import com.fpoly.be_wanren_buffet.dto.VoucherDTO;
import com.fpoly.be_wanren_buffet.dto.response.VoucherResponseDTO;
import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.entity.Promotion;
import com.fpoly.be_wanren_buffet.entity.Voucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }

    public VoucherResponseDTO createVoucher(VoucherDTO voucherDTO) {
        // Fetch Promotion and Customer entities
        Promotion promotion = promotionRepository.findById(voucherDTO.getPromotionId())
                .orElseThrow(() -> new IllegalArgumentException("Promotion not found"));
        Customer customer = customerRepository.findById(voucherDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        String generatedCode = generateCode(10);
        // Create new Voucher entity
        Voucher voucher = new Voucher();
        voucher.setCode(generatedCode);
        voucher.setStatus(voucherDTO.getStatus());
        voucher.setPromotion(promotion);
        voucher.setCustomer(customer);

        // Save Voucher entity
        Voucher savedVoucher = voucherRepository.save(voucher);

        // Map to Response DTO
        return mapToResponseDTO(savedVoucher);
    }

    private VoucherResponseDTO mapToResponseDTO(Voucher voucher) {
        VoucherResponseDTO responseDTO = new VoucherResponseDTO();
        responseDTO.setVoucherId(voucher.getVoucherId());
        responseDTO.setCode(voucher.getCode());
        responseDTO.setStatus(voucher.getStatus());

        if (voucher.getPromotion() != null) {
            responseDTO.setPromotionId(voucher.getPromotion().getPromotion());
        }

        if (voucher.getCustomer() != null) {
            responseDTO.setCustomerId(voucher.getCustomer().getCustomerId());
        }

        return responseDTO;
    }

    public List<PromotionVoucherDTO> getPromotionVoucherInfo(Long customerId) {
        return voucherRepository.findPromotionVoucherInfoByCustomerId(customerId);
    }

}
