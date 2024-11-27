package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dao.OrderDetailRepository;
import com.fpoly.be_wanren_buffet.dao.OrderRepository;
import com.fpoly.be_wanren_buffet.dao.ProductRepository;
import com.fpoly.be_wanren_buffet.dao.ReviewRepository;
import com.fpoly.be_wanren_buffet.dto.*;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.entity.OrderDetail;
import com.fpoly.be_wanren_buffet.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public boolean deleteOrderById(Long id) {
        try {
            orderRepository.deleteById(id);
            return true;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }

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
                orderHistoryDTO.setPayment(order.getPayment().getPaymentMethod().toString());
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

    @Transactional // Procedure này chỉ đọc dữ liệu
    public List<WeeklyRevenueDTO> getWeeklyRevenue() {
        List<Object[]> results = orderRepository.getWeeklyRevenue();
        List<WeeklyRevenueDTO> weeklyRevenues = new ArrayList<>();
        for (Object[] result : results) {
            String dayOfWeek = (String) result[0];
            Double dailyRevenue = result[1] != null ? ((Number) result[1]).doubleValue() : 0.0;
            weeklyRevenues.add(new WeeklyRevenueDTO(dayOfWeek, dailyRevenue));
        }
        return weeklyRevenues;
    }

    @Transactional // Procedure này chỉ đọc dữ liệu
    public List<HourlyRevenueDTO> getHourlyRevenue() {
        List<Object[]> results = orderRepository.getHourlyRevenue();
        List<HourlyRevenueDTO> hourlyRevenues = new ArrayList<>();
        for (Object[] result : results) {
            Integer hourOfDay = null;
            Double hourlyRevenue = 0.0;

            // Kiểm tra và ánh xạ giá trị hour_of_day
            if (result[0] instanceof Number) {
                hourOfDay = ((Number) result[0]).intValue();
            }

            // Kiểm tra và ánh xạ giá trị hourly_revenue
            if (result[1] instanceof Number) {
                hourlyRevenue = ((Number) result[1]).doubleValue();
            }

            hourlyRevenues.add(new HourlyRevenueDTO(hourOfDay, hourlyRevenue));
        }
        return hourlyRevenues;
    }

    public void updateOrderDetails(Long orderId, List<OrderDetailDTO> orderDetails) {


//        // Lấy danh sách các OrderDetail hiện có trong database cho orderId
        List<OrderDetail> existingDetails = orderDetailRepository.findByOrder_orderId(orderId);
//
//        // Duyệt qua các chi tiết hiện có để xóa những cái không còn trong danh sách cập nhật
        for (OrderDetail existingDetail : existingDetails) {
            boolean stillExists = orderDetails.stream()
                    .anyMatch(detail -> detail.getOrderDetailId() != null
                            && detail.getOrderDetailId().equals(existingDetail.getOrderDetailId()));
            if (!stillExists) {
                orderDetailRepository.delete(existingDetail); // Xóa nếu không còn trong danh sách
            }
        }


        for (OrderDetailDTO detail : orderDetails) {
            OrderDetail entity = new OrderDetail();
            entity.setOrder(orderRepository.findByOrderId(orderId));
            entity.setOrderDetailId(detail.getOrderDetailId());
            entity.setProduct(productRepository.findByProductId(detail.getProductId()));
            entity.setQuantity(detail.getQuantity());
            entity.setUnitPrice(detail.getUnitPrice());
            entity.setItemNotes(detail.getItemNotes());
            entity.setUpdatedDate(detail.getUpdatedDate());

            // Thêm logic bổ sung nếu cần, ví dụ: xử lý `_links` hay kiểm tra dữ liệu
            orderDetailRepository.save(entity);
        }

        // Tính lại totalAmount
        double totalAmount = orderDetailRepository.findByOrder_orderId(orderId).stream()
                .mapToDouble(detail -> detail.getQuantity() * detail.getUnitPrice())
                .sum();

        // Cập nhật totalAmount cho Order
        Order order = orderRepository.findByOrderId(orderId);
        order.setTotalAmount(totalAmount+15000);
        orderRepository.save(order); // Lưu lại Order với totalAmount mới

    }



}
