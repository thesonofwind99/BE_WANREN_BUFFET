package com.fpoly.be_wanren_buffet.service.Impl;

import com.fpoly.be_wanren_buffet.converter.OrderConvert;
import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.dao.OrderRepository;
import com.fpoly.be_wanren_buffet.dao.TableRepository;
import com.fpoly.be_wanren_buffet.dao.UserRepository;
import com.fpoly.be_wanren_buffet.dto.request.OrderForStaffRequest;
import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.entity.Tablee;
import com.fpoly.be_wanren_buffet.entity.User;
import com.fpoly.be_wanren_buffet.service.OrderForStaffService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderForStaffServiceImpl implements OrderForStaffService {
    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final TableRepository tableRepository;

    private final CustomerRepository customerRepository;

    @Override
    public Order addOrder(OrderForStaffRequest orderForStaffRequest) {
        Order order = OrderConvert.convertToOrder(orderForStaffRequest);

        if(orderForStaffRequest.getUserId() != null) {
            User user = userRepository.findById(orderForStaffRequest.getUserId()).get();
            order.setUser(user);
        }

        if(orderForStaffRequest.getTableId() != null) {
            Tablee table = tableRepository.findById(orderForStaffRequest.getTableId()).get();
            order.setTablee(table);
        }else{
            order.setTablee(null);
        }

        order.setCreatedDate(LocalDateTime.now());

        return orderRepository.save(order);

    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Long getLatestOrderIdFromTable(Long tableId) {
        PageRequest pageRequest = PageRequest.of(0, 1); // Lấy 1 kết quả duy nhất
        return orderRepository.findLatestOrderIdByTableId(tableId, pageRequest);
    }

    @Override
    public String findOrderStatusById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        return (order != null) ? order.getOrderStatus().name() : null;
    }

    @Override
    @Transactional
    public Double updateTotalPrice(Long orderId, Double totalPrice) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setTotalAmount(totalPrice);
        Order updatedOrder = orderRepository.save(order);
        return updatedOrder.getTotalAmount();
    }


}
