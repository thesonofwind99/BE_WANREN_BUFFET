package com.fpoly.be_wanren_buffet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProducHistorytDTO {
    private Long _productId;
    private String _productName;
    private String _description;
    private Double _price;
    private String _typefood;
    private String _image;
    private Integer _quantity;
    private String _productStatus;
    private Double _total;
}
