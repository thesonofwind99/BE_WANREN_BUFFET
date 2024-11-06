package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dto.request.OrderDetailForStaffRequest;
import com.fpoly.be_wanren_buffet.entity.OrderDetail;
import com.fpoly.be_wanren_buffet.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders_detail_staff")
public class OrderDetailForStaffController {
    private final OrderDetailService orderDetailService;

    @PostMapping("/add/{order_id}")
    public ResponseEntity<?> addOrderDetail(@PathVariable(name = "order_id") Long orderId, @RequestBody List<OrderDetailForStaffRequest> orderDetails) {
        Map<String, Object> response = new HashMap<>();
        response.put("orderDetails", orderDetailService.addOrderDetail(orderId, orderDetails));
        response.put("message", "Thêm chi tiết sản phẩm thành công");
        return ResponseEntity.ok().body(response);
    }
}
