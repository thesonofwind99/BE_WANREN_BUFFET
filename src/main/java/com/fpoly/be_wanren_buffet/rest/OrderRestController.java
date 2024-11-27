package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dao.*;
import com.fpoly.be_wanren_buffet.dto.OrderDetailRequest;
import com.fpoly.be_wanren_buffet.dto.OrderHistoryDTO;
import com.fpoly.be_wanren_buffet.dto.OrderRequest;
import com.fpoly.be_wanren_buffet.dto.ProducHistorytDTO;
import com.fpoly.be_wanren_buffet.entity.*;
import com.fpoly.be_wanren_buffet.enums.OrderStatus;
import com.fpoly.be_wanren_buffet.enums.PaymentMethod;
import com.fpoly.be_wanren_buffet.service.JwtService;
import com.fpoly.be_wanren_buffet.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtService jwtService;



    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, Principal principal) {
        log.debug("Received OrderRequest: {}", orderRequest);
        log.debug("Authenticated user: {}", principal != null ? principal.getName() : "Anonymous");
        System.out.println(orderRequest.getPayment() + "PAYMENT");
        try {
            if (principal == null) {
                // Nếu khách hàng chưa đăng nhập, trả về lỗi
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Bạn cần đăng nhập trước khi đặt hàng.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            // Lấy thông tin khách hàng từ Principal
            String username = principal.getName();

            System.out.println(username + "USER!!!!!");
            if(username.contains("@")){
                Customer customer = customerRepository.findByEmail(username).orElse(null);
                if (customer == null) {
                    // Nếu không tìm thấy khách hàng, trả về lỗi
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", "Không tìm thấy thông tin khách hàng.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                }
                username = customer.getUsername();
            }
            Customer customer = customerRepository.findByUsername(username).orElse(null);

            if (customer == null) {
                // Nếu không tìm thấy khách hàng, trả về lỗi
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Không tìm thấy thông tin khách hàng.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            // Kiểm tra và cập nhật thông tin địa chỉ và số điện thoại nếu cần
            boolean isUpdated = false;
            if (orderRequest.getAddress() != null && !orderRequest.getAddress().equals(customer.getAddress())) {
                customer.setAddress(orderRequest.getAddress());
                isUpdated = true;
            }
            if (orderRequest.getPhone() != null && !orderRequest.getPhone().equals(customer.getPhoneNumber())) {
                customer.setPhoneNumber(orderRequest.getPhone());
                isUpdated = true;
            }
            if (isUpdated) {
                customerRepository.save(customer);
                log.debug("Cập nhật thông tin khách hàng thành công: {}", customer.getUsername());
            }

            // Tạo một đối tượng Order mới
            Order order = new Order();

            // Thiết lập khách hàng cho đơn hàng
            order.setCustomer(customer);
            order.setTotalAmount(orderRequest.getTotalAmount());
            order.setNotes(orderRequest.getNotes());
            order.setAddress(orderRequest.getAddress());
            order.setCreatedDate(LocalDateTime.now());

            // Handle Payment
            Payment payment = new Payment();
            payment.setAmountPaid(orderRequest.getTotalAmount());
            payment.setPaymentStatus(false); // Initially not paid

            // Convert payment method string to enum
            try {
                PaymentMethod paymentMethod = PaymentMethod.valueOf(orderRequest.getPayment().toUpperCase().replace(" ", "_"));
                payment.setPaymentMethod(paymentMethod);
            } catch (IllegalArgumentException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Phương thức thanh toán không hợp lệ.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Associate payment with order
            payment.setOrder(order);
            // payment.setUser(order.getUser()); // Nếu có phương thức getUser()
            order.setPayment(payment);

            // Set order status based on payment method
            if (payment.getPaymentMethod() == PaymentMethod.VNPAY) {
                order.setOrderStatus(OrderStatus.WAITING);
            } else {
                order.setOrderStatus(OrderStatus.PREPARING_ORDER);
            }

            // Save Order (which will cascade and save Payment if properly configured)
            order = orderRepository.save(order);
            log.debug("Saved Order with ID: {}", order.getOrderId());

            // Save Order Details
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

            // Nếu thông tin khách hàng đã được cập nhật, tạo token mới
            String newToken = null;
            if (isUpdated) {
                newToken = jwtService.generateTokenForCustomer(
                        customer.getFullName(),
                        customer.getEmail(),
                        customer.getPhoneNumber(),
                        customer.getCustomerId(),
                        customer.getAddress()
                );
            }

            // Chuẩn bị phản hồi
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.getOrderId());
            response.put("message", "Đặt hàng thành công");
            if (newToken != null) {
                response.put("jwtToken", newToken);
            }

            System.out.println(response + "HELLO");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating order", e);
            // Trả về phản hồi lỗi
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Đã xảy ra lỗi khi tạo đơn hàng.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/GetOrderDetailByOrderId/{orderId}")
    public ResponseEntity<?> getOrderHistory(@PathVariable("orderId") Long orderId) {
        List<ProducHistorytDTO> list = orderService.getOrderDetail(orderId);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/GetOrderByCustomerId/{CustomerId}")
    public ResponseEntity<?> getTest(@PathVariable("CustomerId") Long CustomerId) {
        List<OrderHistoryDTO> orderHistoryDTOList = orderService.orderHistoryDTOList(CustomerId);

        // Kiểm tra nếu không có đơn hàng nào
        if (orderHistoryDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No orders found for Customer ID " + CustomerId);
        }

        return ResponseEntity.ok().body(orderHistoryDTOList);
    }





}
