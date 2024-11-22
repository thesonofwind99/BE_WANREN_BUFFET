package com.fpoly.be_wanren_buffet.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.be_wanren_buffet.dto.ProductDTO;
import com.fpoly.be_wanren_buffet.service.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductRestController {
    @Autowired
    private ProductService productService;

    @GetMapping("/by-category")
    public List<ProductDTO> getProductsByCategory(@RequestParam String categoryName) {
        return productService.getProductsByCategory(categoryName);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        ProductDTO productDTO = productService.getProductById(productId);

        if (productDTO == null) {
            return ResponseEntity.notFound().build(); // Return 404 if product not found
        }

        return ResponseEntity.ok(productDTO); // Return 200 with product details
    }

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/ProductHot")
    public List<Long> getProductsHot() {
        return productService.findAllProductsHot();
    }
}
