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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

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

        if(orderForStaffRequest.getCustomerId() != null){
            Customer customer = customerRepository.findById(orderForStaffRequest.getCustomerId()).get();
            order.setCustomer(customer);
        }else{
            order.setCustomer(null);
        }

        order.setCreatedDate(LocalDateTime.now());

        return orderRepository.save(order);

    }
}
