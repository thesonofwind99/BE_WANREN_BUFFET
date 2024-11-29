package com.fpoly.be_wanren_buffet.dto;

public class PromotionInfoDTO {
    private String promotionName;
    private Double promotionValue;

    public PromotionInfoDTO(String promotionName, Double promotionValue) {
        this.promotionName = promotionName;
        this.promotionValue = promotionValue;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public Double getPromotionValue() {
        return promotionValue;
    }

    public void setPromotionValue(Double promotionValue) {
        this.promotionValue = promotionValue;
    }
}
