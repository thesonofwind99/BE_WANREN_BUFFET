package com.fpoly.be_wanren_buffet.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.fpoly.be_wanren_buffet.entity.OrderDetail;

@RepositoryRestResource(path = "Order_detail")
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    // Tìm chi tiết đơn hàng theo orderId và productId
    Optional<OrderDetail> findByOrder_orderIdAndProduct_productId(Long orderId, Long productId);
    List<OrderDetail> findByOrder_orderId(Long orderId);
    Optional<OrderDetail> findOrderDetailByOrderDetailId(Long orderDetailId);
}
