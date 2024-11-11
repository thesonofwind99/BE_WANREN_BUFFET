package com.fpoly.be_wanren_buffet.dto;

import com.fpoly.be_wanren_buffet.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private String description;
    private Double price;
    private String typeFood;
    private String image;
    private ProductStatus productStatus;
    private Integer quantity;
    private String categoryName; // Assuming you only need the category name
}
