package com.fpoly.be_wanren_buffet.rest.cashier;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.be_wanren_buffet.dao.ProductRepository;
import com.fpoly.be_wanren_buffet.dto.ProductDTO;
import com.fpoly.be_wanren_buffet.entity.Product;

@RestController
@RequestMapping("/Product")
public class ProductRestForCashierController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/all")
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> new ProductDTO(product.getProductId(), product.getProductName(),
                        product.getDescription(), product.getPrice(), product.getTypeFood(), product.getImage(),
                        product.getProductStatus(), product.getQuantity(),
                        product.getCategory() != null ? product.getCategory().getCategoryName() : null))
                .collect(Collectors.toList());
    }

}
