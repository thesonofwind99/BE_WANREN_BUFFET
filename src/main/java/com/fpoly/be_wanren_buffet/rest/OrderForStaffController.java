package com.fpoly.be_wanren_buffet.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.be_wanren_buffet.dto.UpdateCustomerRequestStaff;
import com.fpoly.be_wanren_buffet.dto.request.OrderForStaffRequest;
import com.fpoly.be_wanren_buffet.dto.request.OrderUpdateRequest;
import com.fpoly.be_wanren_buffet.dto.request.TransferRequest;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.entity.Tablee;
import com.fpoly.be_wanren_buffet.enums.OrderStatus;
import com.fpoly.be_wanren_buffet.enums.TableStatus;
import com.fpoly.be_wanren_buffet.service.OrderForStaffService;
import com.fpoly.be_wanren_buffet.service.OrderService;
import com.fpoly.be_wanren_buffet.service.TableService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/order_staff")
public class OrderForStaffController {
    private OrderForStaffService orderForStaffService;

    private OrderService orderService;

    private TableService tableService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addOrder(@RequestBody OrderForStaffRequest orderForStaffRequest) {
        Order order = orderForStaffService.addOrder(orderForStaffRequest);
        Map<String, Object> response = new HashMap<>();
        response.put("id", order.getOrderId());
        response.put("message", "Thêm order thành công");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId,
            @RequestBody OrderUpdateRequest orderUpdateRequest) {
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

    @PutMapping("/update/total_amount/{order_id}/{total_amount}")
    public ResponseEntity<Map<String, Object>> updateOrderTotalAmount(@PathVariable(name = "order_id") Long orderId,
            @PathVariable(name = "total_amount") Double total_amount) {
        Map<String, Object> response = new HashMap<>();
        response.put("amount_last", orderForStaffService.updateTotalPrice(orderId, total_amount));
        response.put("message", "Cập nhật tổng tiền thành công");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/transfer")
    public ResponseEntity<String> transferTable(
            @PathVariable Long orderId,
            @RequestBody TransferRequest transferRequest) {

        // Tìm đơn hàng theo orderId
        Order order = orderForStaffService.findOrderById(orderId);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        // Tìm bàn cũ và bàn mới theo tableId
        Tablee oldTable = order.getTablee();
        Tablee newTable = tableService.findTableById(transferRequest.getNewTableId());

        if (newTable == null || newTable.getTableStatus() != TableStatus.EMPTY_TABLE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Table is not available");
        }

        // Cập nhật trạng thái bàn cũ và bàn mới
        oldTable.setTableStatus(TableStatus.EMPTY_TABLE); // Bàn cũ trở thành bàn trống
        newTable.setTableStatus(TableStatus.OCCUPIED_TABLE); // Bàn mới trở thành bàn đã chiếm

        // Lưu thay đổi trạng thái của các bàn
        tableService.save(oldTable);
        tableService.save(newTable);

        // Cập nhật bàn mới cho đơn hàng
        order.setTablee(newTable);

        // Lưu lại đơn hàng với bàn mới
        orderForStaffService.save(order);

        return ResponseEntity.ok("Order transferred and tables updated successfully");
    }

    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        boolean isUpdated = orderForStaffService.updateOrderStatus(orderId, status);

        if (isUpdated) {
            return ResponseEntity.ok("Order status updated successfully");
        } else {
            return ResponseEntity.status(400).body("Failed to update order status");
        }
    }

    @PutMapping("/update-customer")
    public ResponseEntity<String> updateCustomerInOrder(@RequestBody UpdateCustomerRequestStaff request) {
        orderForStaffService.updateCustomerInOrder(request.getOrderId(), request.getPhoneNumber());
        return ResponseEntity.ok("Customer updated successfully for orderId: " + request.getOrderId());
    }

    @GetMapping("/check-customer")
    public ResponseEntity<String> checkCustomerExistenceByOrderId(@RequestParam("orderId") Long orderId) {
        boolean exists = orderForStaffService.doesCustomerExistByOrderId(orderId);

        if (exists) {
            return ResponseEntity.ok("Customer exists for the specified order.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer does not exist for the specified order.");
        }
    }

    @PutMapping("/update-discount-points-order")
    public ResponseEntity<String> updateDiscountPoints(
            @RequestParam("orderId") Long orderId,
            @RequestParam("discountPoints") Long discountPoints) {
        boolean isUpdated = orderForStaffService.updateDiscountPoints(orderId, discountPoints);

        if (isUpdated) {
            return ResponseEntity.ok("Discount points updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found or update failed.");
        }
    }

    @GetMapping("/get-discount-points")
    public ResponseEntity<?> getDiscountPoints(@RequestParam("orderId") Long orderId) {
        Optional<Long> discountPoints = orderForStaffService.getDiscountPointsByOrderId(orderId);

        if (discountPoints.isPresent()) {
            return ResponseEntity.ok(discountPoints.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found or discount points not set.");
        }
    }
}
