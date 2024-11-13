package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dto.request.OrderForStaffRequest;
import com.fpoly.be_wanren_buffet.dto.request.OrderUpdateRequest;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.service.OrderForStaffService;
import com.fpoly.be_wanren_buffet.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/order_staff")
public class OrderForStaffController {
    private OrderForStaffService orderForStaffService;

    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addOrder(@RequestBody OrderForStaffRequest orderForStaffRequest) {
        Order order = orderForStaffService.addOrder(orderForStaffRequest);
        Map<String, Object> response = new HashMap<>();
        response.put("id", order.getOrderId());
        response.put("message", "Thêm order thành công");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody OrderUpdateRequest orderUpdateRequest) {
        try {
            // Find the order by ID
            Optional<Order> existingOrder = orderForStaffService.findById(orderId);

            if (existingOrder.isEmpty()) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }

            // Update the order's total amount
            Order order = existingOrder.get();
            order.setTotalAmount(orderUpdateRequest.getTotalAmount());

            // Save the updated order
            orderForStaffService.save(order);

            return new ResponseEntity<>("Order updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findOrderIdByTableId/{tableId}")
    public Long getLatestOrderId(@PathVariable Long tableId) {
        return orderForStaffService.getLatestOrderIdFromTable(tableId);
    }

    @GetMapping("/status/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderStatus(@PathVariable Long orderId) {
        Map<String, Object> response = new HashMap<>();

        try {
            String orderStatus = orderForStaffService.findOrderStatusById(orderId);

            if (orderStatus != null) {
                response.put("orderStatus", orderStatus);
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Order not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("error", "Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/get_amount/{order_id}")
    public ResponseEntity<Map<String, Object>> getOrderAmount(@PathVariable(name = "order_id") Long orderId) {
        Map<String, Object> response = new HashMap<>();
        response.put("amount", orderService.GetOrderById(orderId).getTotalAmount());
        response.put("message", "Lấy tổng tiền thành công");
        return ResponseEntity.ok(response);
    }

}
