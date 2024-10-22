package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "Product")
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p.productId FROM Product p " +
            "JOIN OrderDetail od ON od.product.productId = p.productId " +
            "GROUP BY p.productId " +
            "ORDER BY SUM(od.quantity) DESC")
    List<Long> findMostOrderedProduct(Pageable pageable); // Sử dụng Pageable để giới hạn số lượng kết quả

    List<Product> findByTypeFood(String typeFood);

    Page<Product> findByProductNameContaining(String productName, Pageable pageable); // Đổi tên phương thức để phù hợp với thuộc tính 'productName'
}
