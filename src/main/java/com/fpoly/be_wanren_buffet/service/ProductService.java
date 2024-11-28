package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dao.CategoryRepository;
import com.fpoly.be_wanren_buffet.dao.ProductRepository;
import com.fpoly.be_wanren_buffet.dto.ProductDTO;
import com.fpoly.be_wanren_buffet.entity.Category;
import com.fpoly.be_wanren_buffet.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    public ProductDTO convertToProductDTO(Product product) {
        return new ProductDTO(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getTypeFood(),
                product.getImage(),
                product.getProductStatus(),
                product.getQuantity(),
                product.getCategory() != null ? product.getCategory().getCategoryName() : null
        );
    }

    public List<ProductDTO> getProductsByCategory(String categoryName) {
        List<Product> products = productRepository.findProductByCategoryName(categoryName);
        return products.stream()
                .map(this::convertToProductDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findByProductId(productId);

        if (product == null) {
            return null; // Return null if product not found
        }

        // Convert the Product entity to ProductDTO
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setTypeFood(product.getTypeFood());
        productDTO.setImage(product.getImage());
        productDTO.setProductStatus(product.getProductStatus());
        productDTO.setQuantity(product.getQuantity());

        return productDTO;
    }

    public List<Long> findAllProductsHot() {
      return   productRepository.findMostOrderedProduct(PageRequest.of(0, 4));
    }

    @Transactional
    public void updateCategoryId(Long productId, Long categoryId) {
        // Lấy sản phẩm theo productId
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // Lấy danh mục theo categoryId
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        // Cập nhật danh mục của sản phẩm
        product.setCategory(category);

        // Lưu thay đổi
        productRepository.save(product);
    }
}
