package com.fpoly.be_wanren_buffet.rest.cashier;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.be_wanren_buffet.dao.OrderDetailRepository;
import com.fpoly.be_wanren_buffet.dao.OrderRepository;
import com.fpoly.be_wanren_buffet.dto.OrderDTO;
import com.fpoly.be_wanren_buffet.dto.OrderDetailDTO;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.entity.OrderDetail;
import com.fpoly.be_wanren_buffet.service.OrderService;

@RestController
@RequestMapping("/Orders")
public class OrderRestForCashierController {

        @Autowired
        private OrderService orderService;

        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private OrderDetailRepository orderDetailRepository;

        @PutMapping("/{orderId}/updateOrder")
        public ResponseEntity<?> updateOrderDetails(@PathVariable Long orderId,
                        @RequestBody List<OrderDetailDTO> orderDetails) {
                orderService.updateOrderDetails(orderId, orderDetails); // Gọi service xử lý
                return ResponseEntity.noContent().build(); // HTTP 204
        }

        @GetMapping("/all")
        public List<OrderDTO> getAllOrders() {
                List<Order> orders = orderRepository.findAll();
                return orders.stream()
                                .map(order -> new OrderDTO(order.getCreatedDate(), order.getOrderId(),
                                                order.getOrderStatus(),
                                                order.getTotalAmount(), order.getNotes(), order.getAddress(),
                                                order.getDiscountPointUsed(),
                                                order.getNumberPeople(),
                                                order.getCustomer() != null ? order.getCustomer().getPhoneNumber()
                                                                : null, // Số điện thoại
                                                                        // khách hàng
                                                order.getCustomer() != null ? order.getCustomer().getFullName() : null, // Tên
                                                                                                                        // đầy
                                                                                                                        // đủ
                                                                                                                        // khách
                                                                                                                        // hàng
                                                order.getTablee() != null ? order.getTablee().getTableId() : null))
                                .collect(Collectors.toList());
        }

        @GetMapping("/allWithTableNull")
        public List<OrderDTO> getAllOrdersWithTableNull() {
                System.out.println("Fetching orders with table null...");
                List<Order> orders = orderRepository.findAll();
                return orders.stream()
                                .filter(order -> order.getTablee() == null) // Lọc các đơn hàng có tablee là null
                                .map(order -> new OrderDTO(
                                                order.getCreatedDate(),
                                                order.getOrderId(),
                                                order.getOrderStatus(),
                                                order.getTotalAmount(),
                                                order.getNotes(),
                                                order.getAddress(),
                                                order.getDiscountPointUsed(),
                                                order.getNumberPeople(),
                                                order.getCustomer() != null ? order.getCustomer().getPhoneNumber()
                                                                : null,
                                                order.getCustomer() != null ? order.getCustomer().getFullName() : null,
                                                null))
                                .collect(Collectors.toList());
        }

        @GetMapping("/{orderId}/orderDetails")
        public ResponseEntity<List<OrderDetailDTO>> getOrderDetailsByOrderId(@PathVariable Long orderId) {
                List<OrderDetail> orderDetails = orderDetailRepository.findByOrder_orderId(orderId);

                // Mapping OrderDetail to OrderDetailDTO
                List<OrderDetailDTO> orderDetailDTOs = orderDetails.stream()
                                .map(orderDetail -> new OrderDetailDTO(
                                                orderDetail.getOrder().getOrderId(),
                                                orderDetail.getOrderDetailId(),
                                                orderDetail.getProduct().getProductId(),
                                                orderDetail.getQuantity(),
                                                orderDetail.getUnitPrice(),
                                                orderDetail.getItemNotes(),
                                                orderDetail.getCreatedDate().toString(),
                                                orderDetail.getUpdatedDate(),
                                                orderDetail.getProduct().getProductName(),
                                                orderDetail.getProduct().getImage()))
                                .collect(Collectors.toList());

                return ResponseEntity.ok(orderDetailDTOs);
        }

}
