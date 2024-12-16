package com.fpoly.be_wanren_buffet.service;

import java.util.Optional;

import com.fpoly.be_wanren_buffet.dto.request.OrderForStaffRequest;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.enums.OrderStatus;

public interface OrderForStaffService {
    Order addOrder(OrderForStaffRequest orderForStaffRequest);
    // Find an order by ID
    Optional<Order> findById(Long orderId);

    // Save the updated order
    Order save(Order order);

    Long getLatestOrderIdFromTable(Long tableId);

    String findOrderStatusById(Long orderId);

    Double updateTotalPrice(Long orderId, Double totalPrice);

    Order findOrderById(Long orderId);

    boolean updateOrderStatus(Long orderId, OrderStatus status);

    void updateCustomerInOrder(Long orderId, String phoneNumber);

    boolean doesCustomerExistByOrderId(Long orderId);

    boolean updateDiscountPoints(Long orderId, Long discountPoints);

    Optional<Long> getDiscountPointsByOrderId(Long orderId);
}
