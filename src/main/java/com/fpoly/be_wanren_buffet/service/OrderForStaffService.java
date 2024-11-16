package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dto.request.OrderForStaffRequest;
import com.fpoly.be_wanren_buffet.entity.Order;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public interface OrderForStaffService {
    Order addOrder(OrderForStaffRequest orderForStaffRequest);
    // Find an order by ID
    Optional<Order> findById(Long orderId);

    // Save the updated order
    Order save(Order order);

    Long getLatestOrderIdFromTable(Long tableId);

    String findOrderStatusById(Long orderId);
}
