package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dao.OrderDetailRepository;
import com.fpoly.be_wanren_buffet.dao.OrderRepository;
import com.fpoly.be_wanren_buffet.dao.ProductRepository;
import com.fpoly.be_wanren_buffet.dao.ReviewRepository;
import com.fpoly.be_wanren_buffet.dto.OrderHistoryDTO;
import com.fpoly.be_wanren_buffet.dto.ProducHistorytDTO;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.entity.OrderDetail;
import com.fpoly.be_wanren_buffet.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing orders.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetaillService orderDetaillService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public void UpdateStatusOrder(Order order) {
        orderRepository.save(order);
    }

    public Order GetOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order with ID " + id + " not found"));
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    /**
     * Retrieves the order details for a given order ID.
     *
     * @param orderId The ID of the order.
     * @return A list of product history DTOs.
     */
    public List<ProducHistorytDTO> getOrderDetail(Long orderId) {
        List<OrderDetail> listOrderDetails = orderDetaillService.findByOrderDetailByOrderId(orderId);
        List<ProducHistorytDTO> listProducts = new ArrayList<>();
        for (OrderDetail orderDetail : listOrderDetails) {
            if (orderDetail.getOrder().getOrderId().equals(orderId)) {
                ProducHistorytDTO producHistorytDTO = new ProducHistorytDTO(
                        orderDetail.getProduct().getProductId(),
                        orderDetail.getProduct().getProductName(),
                        orderDetail.getProduct().getDescription(),
                        orderDetail.getProduct().getPrice(),
                        orderDetail.getProduct().getTypeFood(),
                        orderDetail.getProduct().getImage(),
                        orderDetail.getQuantity(),
                        orderDetail.getProduct().getProductStatus().toString(),
                        orderDetail.getOrder().getTotalAmount()
                );
                listProducts.add(producHistorytDTO);
            }
        }
        return listProducts;
    }

    /**
     * Retrieves the order history for a given customer ID, including the review status.
     *
     * @param customerId The ID of the customer.
     * @return A list of order history DTOs.
     */
    public List<OrderHistoryDTO> orderHistoryDTOList(Long customerId) {
        List<Order> orderList = orderRepository.findAll();
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        List<OrderHistoryDTO> orderHistoryDTOList = new ArrayList<>();
        List<Review> reviewList = reviewRepository.findAll();

        // Create a list of order IDs that have been reviewed
        List<Long> reviewedOrderIds = new ArrayList<>();
        for (Review review : reviewList) {
            if (review.getOrder() != null) {
                reviewedOrderIds.add(review.getOrder().getOrderId());
            }
        }

        // Populate the order history list based on the customer ID
        for (Order order : orderList) {
            if (order.getCustomer() != null && order.getCustomer().getCustomerId().equals(customerId) && order.getOrderStatus().toString().equals("PREPARING_ORDER")) {
                OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO();
                orderHistoryDTO.setOrderId(order.getOrderId());
                orderHistoryDTO.setTotalAmount(order.getTotalAmount());
                orderHistoryDTO.setPayment(order.getPayment());
                orderHistoryDTO.setAddress(order.getAddress());
                orderHistoryDTO.setNotes(order.getNotes());
                orderHistoryDTO.setProducHistorytDTOList(new ArrayList<>()); // Initialize list for product history

                // Check if the order has been reviewed
                boolean isReviewed = reviewedOrderIds.contains(order.getOrderId());
                orderHistoryDTO.setIsReviewed(isReviewed); // Set the isReviewed flag

                orderHistoryDTOList.add(orderHistoryDTO);
            }
        }

        // Populate product history for each order
        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetail.getOrder() != null) {
                for (OrderHistoryDTO orderHistoryDTO : orderHistoryDTOList) {
                    if (orderDetail.getOrder().getOrderId().equals(orderHistoryDTO.getOrderId())) {
                        ProducHistorytDTO producHistorytDTO = new ProducHistorytDTO(
                                orderDetail.getProduct().getProductId(),
                                orderDetail.getProduct().getProductName(),
                                orderDetail.getProduct().getDescription(),
                                orderDetail.getProduct().getPrice(),
                                orderDetail.getProduct().getTypeFood(),
                                orderDetail.getProduct().getImage(),
                                orderDetail.getQuantity(),
                                orderDetail.getProduct().getProductStatus().toString(),
                                orderDetail.getOrder().getTotalAmount()
                        );

                        // Add each product history to the list within the order history
                        orderHistoryDTO.getProducHistorytDTOList().add(producHistorytDTO);
                    }
                }
            }
        }

        return orderHistoryDTOList;
    }

    public Map<Integer, Double> getMonthlyRevenue(int year) {
        List<Object[]> results = orderRepository.getMonthlyRevenue(2024);
        Map<Integer, Double> revenueByMonth = new HashMap<>();

        // Khởi tạo tất cả các tháng với doanh thu là 0
        for (int i = 1; i <= 12; i++) {
            revenueByMonth.put(i, 0.0);
        }

        // Điền dữ liệu thực tế vào
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Double revenue = (Double) result[1];
            revenueByMonth.put(month, revenue);
        }

        return revenueByMonth;
    }


}
