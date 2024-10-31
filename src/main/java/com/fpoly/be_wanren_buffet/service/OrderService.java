package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dao.OrderRepository;
import com.fpoly.be_wanren_buffet.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void UpdateStatusOrder(Order order) {
        orderRepository.save(order);
    }

    public Order GetOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order with ID " + id + " not found"));
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }
}
