package com.fpoly.be_wanren_buffet.rest.cashier;
import com.fpoly.be_wanren_buffet.dao.ProductRepository;
import com.fpoly.be_wanren_buffet.dto.OrderDetailDTO;
import com.fpoly.be_wanren_buffet.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Orders")
public class OrderRestForCashierController {

    @Autowired
    private OrderService orderService;

    @PutMapping("/{orderId}/updateOrder")
    public ResponseEntity<?> updateOrderDetails(@PathVariable Long orderId, @RequestBody List<OrderDetailDTO> orderDetails) {
        orderService.updateOrderDetails(orderId, orderDetails); // Gọi service xử lý
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
