package com.fpoly.be_wanren_buffet.dto.request;

public class TransferRequest {

    private Long orderId;
    private Long newTableId;

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getNewTableId() {
        return newTableId;
    }

    public void setNewTableId(Long newTableId) {
        this.newTableId = newTableId;
    }
}