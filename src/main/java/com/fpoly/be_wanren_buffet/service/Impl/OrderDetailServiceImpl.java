package com.fpoly.be_wanren_buffet.service.Impl;

import com.fpoly.be_wanren_buffet.converter.OrderDetailConvert;
import com.fpoly.be_wanren_buffet.dao.OrderDetailRepository;
import com.fpoly.be_wanren_buffet.dao.OrderRepository;
import com.fpoly.be_wanren_buffet.dao.ProductRepository;
import com.fpoly.be_wanren_buffet.dto.request.OrderDetailForStaffRequest;
import com.fpoly.be_wanren_buffet.dto.response.OrderDetailForStaffResponse;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.entity.OrderDetail;
import com.fpoly.be_wanren_buffet.entity.Product;
import com.fpoly.be_wanren_buffet.service.OrderDetailService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    //Repo order details
    private final OrderDetailRepository orderDetailRepository;
    //Repo order
    private final OrderRepository orderRepository;
    //Repo product
    private final ProductRepository productRepository;

    @Override
    public List<OrderDetail> addOrderDetail(Long orderId, List<OrderDetailForStaffRequest> orderDetails) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        List<OrderDetail> orderDetailList = new ArrayList<>();

        for(OrderDetailForStaffRequest orderDetail : orderDetails){
            OrderDetail orderDetailEntity = OrderDetailConvert.convertToOrderDetail(orderDetail);

            //Add product for OrderDetail
            if(orderDetail.getProductId() != null){
                Product product = productRepository.findById(orderDetail.getProductId()).get();
                orderDetailEntity.setProduct(product);
            }

            //Add order for OrderDetail
            orderDetailEntity.setOrder(order);

            orderDetailRepository.save(orderDetailEntity);
            orderDetailList.add(orderDetailEntity);
        }
        return orderDetailList;
    }

    @Override
    public List<OrderDetail> addOrUpdateOrderDetail(Long orderId, List<OrderDetailForStaffRequest> orderDetails) {
        return orderDetails.stream().map(orderDetailRequest -> {
            // Lấy thực thể Order từ orderId
            Optional<Order> order = orderRepository.findById(orderId);
            if (!order.isPresent()) {
                throw new RuntimeException("Order not found with id " + orderId); // Xử lý khi không tìm thấy đơn hàng
            }

            // Lấy thực thể Product từ productId
            Optional<Product> product = productRepository.findById(orderDetailRequest.getProductId());
            if (!product.isPresent()) {
                throw new RuntimeException("Product not found with id " + orderDetailRequest.getProductId()); // Xử lý khi không tìm thấy sản phẩm
            }

            // Tìm chi tiết đơn hàng hiện tại theo orderId và productId
            Optional<OrderDetail> existingOrderDetail = orderDetailRepository.findByOrder_orderIdAndProduct_productId(orderId, orderDetailRequest.getProductId());

            if (existingOrderDetail.isPresent()) {
                // Nếu đã tồn tại, cập nhật số lượng
                OrderDetail orderDetail = existingOrderDetail.get();
                orderDetail.setQuantity(orderDetail.getQuantity() + orderDetailRequest.getQuantity()); // Cập nhật số lượng
                return orderDetailRepository.save(orderDetail);
            } else {
                // Nếu không tồn tại, tạo mới
                OrderDetail orderDetail = OrderDetailConvert.convertToOrderDetail(orderDetailRequest);
                orderDetail.setOrder(order.get());  // Gán Order cho OrderDetail
                orderDetail.setProduct(product.get()); // Gán Product cho OrderDetail
                orderDetail.setQuantity(orderDetailRequest.getQuantity());
                orderDetail.setUnitPrice(orderDetailRequest.getUnitPrice());
                orderDetail.setItemNotes(orderDetailRequest.getItemNotes());
                return orderDetailRepository.save(orderDetail);
            }
        }).collect(Collectors.toList());
    }

    public List<OrderDetailForStaffRequest> getOrderDetailsByOrderId(Long orderId) {
        // Truy vấn OrderDetail và chuyển đổi thành OrderDetailDTO
        return orderDetailRepository.findByOrder_orderId(orderId).stream()
                .map(orderDetail -> {
                    OrderDetailForStaffRequest dto = new OrderDetailForStaffRequest();
                    dto.setOrderId(orderDetail.getOrder().getOrderId());
                    dto.setProductId(orderDetail.getProduct().getProductId());  // Lấy productId
                    dto.setQuantity(orderDetail.getQuantity());
                    dto.setUnitPrice(orderDetail.getUnitPrice());
                    dto.setItemNotes(orderDetail.getItemNotes());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDetailForStaffResponse> getOrderDetailsWithNameProduct(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        List<OrderDetailForStaffResponse> orderDetailForStaffResponseList = new ArrayList<>();
        for(OrderDetail orderDetail : orderDetailRepository.findByOrder_orderId(orderId)){
            OrderDetailForStaffResponse orderDetailForStaffResponse = new OrderDetailForStaffResponse();

            orderDetailForStaffResponse.setProductName(orderDetail.getProduct().getProductName());
            orderDetailForStaffResponse.setQuantity(orderDetail.getQuantity());
            orderDetailForStaffResponse.setPrice(orderDetail.getUnitPrice());

            orderDetailForStaffResponseList.add(orderDetailForStaffResponse);
        }
        return orderDetailForStaffResponseList;

    }
}
