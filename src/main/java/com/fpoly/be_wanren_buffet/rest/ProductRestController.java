package com.fpoly.be_wanren_buffet.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/UpdateCategory")
    public ResponseEntity<?> updateCategory(
            @RequestParam("productId") Long productId,
            @RequestParam("categoryId") Long categoryId) {

        try {
            // Kiểm tra nếu có giá trị hợp lệ cho productId và categoryId
            if (productId == null || categoryId == null) {
                return ResponseEntity.badRequest().body("ProductId và CategoryId là bắt buộc.");
            }

            // Gọi service để cập nhật danh mục sản phẩm
            productService.updateCategoryId(productId, categoryId);
            return ResponseEntity.ok("Danh mục của sản phẩm đã được cập nhật.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi xảy ra khi cập nhật danh mục.");
        }
    }



}
