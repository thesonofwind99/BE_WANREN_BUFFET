package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryRestResource(path = "Product")
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p.productId FROM Product p " +
            "JOIN OrderDetail od ON od.product.productId = p.productId " +
            "WHERE p.price > 10000 " + // Thêm điều kiện lọc giá sản phẩm > 100,000
            "GROUP BY p.productId " +
            "ORDER BY SUM(od.quantity) DESC")
    List<Long> findMostOrderedProduct(Pageable pageable);
    // Sử dụng Pageable để giới hạn số lượng kết quả

    Product findByProductId(Long productId);

    List<Product> findByTypeFood(String typeFood);

    Page<Product> findByProductNameContaining(String productName, Pageable pageable); // Đổi tên phương thức để phù hợp với thuộc tính 'productName'

    @Query("SELECT p FROM Product p WHERE p.category.categoryName = ?1")
    List<Product> findProductByCategoryName(String categoryName);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.category.categoryId = :categoryId WHERE p.productId = :productId")
    void updateCategoryId(@Param("productId") Long productId, @Param("categoryId") Long categoryId);



}
