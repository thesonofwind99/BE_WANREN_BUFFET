package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dto.request.OrderForStaffRequest;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.service.OrderForStaffService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/order_staff")
public class OrderForStaffController {
    private OrderForStaffService orderForStaffService;

    @PostMapping("/add")
    public ResponseEntity<Order> addOrder(@RequestBody OrderForStaffRequest orderForStaffRequest) {
        Order order = orderForStaffService.addOrder(orderForStaffRequest);
        return ResponseEntity.ok(order);
    }

}
