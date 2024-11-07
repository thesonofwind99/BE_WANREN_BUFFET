package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dao.OrderDetailRepository;
import com.fpoly.be_wanren_buffet.dao.OrderRepository;
import com.fpoly.be_wanren_buffet.dao.ProductRepository;
import com.fpoly.be_wanren_buffet.dto.OrderHistoryDTO;
import com.fpoly.be_wanren_buffet.dto.ProducHistorytDTO;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.entity.OrderDetail;
import com.fpoly.be_wanren_buffet.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<ProducHistorytDTO> getOrderDetail(Long orderId) {
        List<OrderDetail> listOrderDetails = orderDetaillService.findByOrderDetailByOrderId(orderId);
        List<ProducHistorytDTO> listProducts = new ArrayList<>();
        for (OrderDetail orderDetail : listOrderDetails){
            if(orderDetail.getOrder().getOrderId().equals(orderId)){
                ProducHistorytDTO producHistorytDTO = new ProducHistorytDTO(orderDetail.getProduct().getProductId()
                        ,orderDetail.getProduct().getProductName()
                        ,orderDetail.getProduct().getDescription()
                        ,orderDetail.getProduct().getPrice()
                        ,orderDetail.getProduct().getTypeFood(),
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

    public List<OrderHistoryDTO> orderHistoryDTOList(Long customerId) {
        List<Order> orderList = orderRepository.findAll();
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        List<OrderHistoryDTO> orderHistoryDTOList = new ArrayList<>();

        // Populate the order history list based on the customer ID
        for (Order order : orderList) {
            if (order.getCustomer().getCustomerId().equals(customerId)) {
                OrderHistoryDTO orderHistoryDTO = new OrderHistoryDTO();
                orderHistoryDTO.setOrderId(order.getOrderId());
                orderHistoryDTO.setTotalAmount(order.getTotalAmount());
                orderHistoryDTO.setPayment(order.getPayment());
                orderHistoryDTO.setNotes(order.getNotes());
                orderHistoryDTO.setProducHistorytDTOList(new ArrayList<>()); // Initialize list for product history
                orderHistoryDTOList.add(orderHistoryDTO);
            }
        }

        // Populate product history for each order
        for (OrderDetail orderDetail : orderDetails) {
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

        return orderHistoryDTOList;
    }


}


