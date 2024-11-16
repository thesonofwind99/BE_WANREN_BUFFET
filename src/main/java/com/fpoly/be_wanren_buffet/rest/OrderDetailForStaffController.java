package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dto.request.OrderDetailForStaffRequest;
import com.fpoly.be_wanren_buffet.dto.response.OrderDetailForStaffResponse;
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

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {
        List<OrderDetailForStaffRequest> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
        return ResponseEntity.ok(orderDetails);
    }

    @PostMapping("/add/{order_id}")
    public ResponseEntity<?> addOrderDetail(@PathVariable(name = "order_id") Long orderId, @RequestBody List<OrderDetailForStaffRequest> orderDetails) {
        Map<String, Object> response = new HashMap<>();
        response.put("orderDetails", orderDetailService.addOrderDetail(orderId, orderDetails));
        response.put("message", "Thêm chi tiết sản phẩm thành công");
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/add_or_update/{order_id}")
    public ResponseEntity<?> addOrUpdateOrderDetail(@PathVariable(name = "order_id") Long orderId, @RequestBody List<OrderDetailForStaffRequest> orderDetails) {
        Map<String, Object> response = new HashMap<>();
        // Gọi service để xử lý thêm mới hoặc cập nhật chi tiết đơn hàng
        List<OrderDetail> updatedOrderDetails = orderDetailService.addOrUpdateOrderDetail(orderId, orderDetails);
        response.put("orderDetails", updatedOrderDetails);
        response.put("message", "Thêm hoặc cập nhật chi tiết sản phẩm thành công");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get/order_details/with_name/{order_id}")
    public ResponseEntity<?> getOrderDetailsWithNameByOrderId(@PathVariable(name = "order_id") Long orderId) {
        Map<String, Object> response = new HashMap<>();
        List<OrderDetailForStaffResponse> orderDetailForStaffResponses = orderDetailService.getOrderDetailsWithNameProduct(orderId);
        response.put("orderDetails", orderDetailForStaffResponses);
        response.put("message", "Lấy dữ liệu thành công");
        return ResponseEntity.ok().body(response);
    }
}
