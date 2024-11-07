package com.fpoly.be_wanren_buffet.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderHistoryDTO {
    private Long orderId;
    private Double totalAmount;
    private String notes;
    private String address;
    private String payment;
    private List<ProducHistorytDTO> producHistorytDTOList;
}
