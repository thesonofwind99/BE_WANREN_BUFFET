package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dao.*;
import com.fpoly.be_wanren_buffet.dto.OrderDetailRequest;
import com.fpoly.be_wanren_buffet.dto.OrderRequest;
import com.fpoly.be_wanren_buffet.entity.*;
import com.fpoly.be_wanren_buffet.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, Principal principal) {
        log.debug("Received OrderRequest: {}", orderRequest);
        log.debug("Authenticated user: {}", principal != null ? principal.getName() : "Anonymous");

        try {
            if (principal == null) {
                // Nếu khách hàng chưa đăng nhập, trả về lỗi
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Bạn cần đăng nhập trước khi đặt hàng.");
                return ResponseEntity.status(401).body(errorResponse);
            }

            // Lấy thông tin khách hàng từ Principal
            String username = principal.getName();
            Customer customer = customerRepository.findByUsername(username);

            if (customer == null) {
                // Nếu không tìm thấy khách hàng, trả về lỗi
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Không tìm thấy thông tin khách hàng.");
                return ResponseEntity.status(404).body(errorResponse);
            }

            // Tạo một đối tượng Order mới
            Order order = new Order();

            // Thiết lập khách hàng cho đơn hàng
            order.setCustomer(customer);

            // Thiết lập thông tin đơn hàng
            order.setTotalAmount(orderRequest.getTotalAmount());
            order.setNotes(orderRequest.getNotes());
            order.setPayment(orderRequest.getPayment());
            order.setAddress(orderRequest.getAddress());
            if (orderRequest.getPayment().equals("VN PAY")){
                order.setOrderStatus(OrderStatus.WAITING);
            }else {
                order.setOrderStatus(OrderStatus.PREPARING_ORDER);
            }

            order.setCreatedDate(LocalDateTime.now());

            // Lưu đơn hàng
            order = orderRepository.save(order);
            log.debug("Saved Order with ID: {}", order.getOrderId());

            // Lưu chi tiết đơn hàng
            for (OrderDetailRequest detailRequest : orderRequest.getOrderDetails()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setQuantity(detailRequest.getQuantity());
                orderDetail.setUnitPrice(detailRequest.getUnitPrice());
                orderDetail.setItemNotes(detailRequest.getItemNotes());
                orderDetail.setCreatedDate(LocalDateTime.now());
                Product product = productRepository.findById(detailRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + detailRequest.getProductId()));
                orderDetail.setProduct(product);

                orderDetailRepository.save(orderDetail);
                log.debug("Saved OrderDetail for Product ID: {}", product.getProductId());
            }

            // Trả về phản hồi thành công
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.getOrderId());
            response.put("message", "Đặt hàng thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating order", e);
            // Trả về phản hồi lỗi
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Đã xảy ra lỗi khi tạo đơn hàng.");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
