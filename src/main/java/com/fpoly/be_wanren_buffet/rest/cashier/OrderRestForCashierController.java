package com.fpoly.be_wanren_buffet.rest.cashier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.be_wanren_buffet.dto.OrderDetailDTO;
import com.fpoly.be_wanren_buffet.service.OrderService;

@RestController
@RequestMapping("/Orders")
public class OrderRestForCashierController {

    @Autowired
    private OrderService orderService;

    @PutMapping("/{orderId}/updateOrder")
    public ResponseEntity<?> updateOrderDetails(@PathVariable Long orderId,
            @RequestBody List<OrderDetailDTO> orderDetails) {
        orderService.updateOrderDetails(orderId, orderDetails); // Gọi service xử lý
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
