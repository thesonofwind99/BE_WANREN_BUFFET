package com.fpoly.be_wanren_buffet.service.Impl;

import com.fpoly.be_wanren_buffet.converter.OrderDetailConvert;
import com.fpoly.be_wanren_buffet.dao.OrderDetailRepository;
import com.fpoly.be_wanren_buffet.dao.OrderRepository;
import com.fpoly.be_wanren_buffet.dao.ProductRepository;
import com.fpoly.be_wanren_buffet.dto.OrderDetailRequest;
import com.fpoly.be_wanren_buffet.dto.request.OrderDetailForStaffRequest;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.entity.OrderDetail;
import com.fpoly.be_wanren_buffet.entity.Product;
import com.fpoly.be_wanren_buffet.service.OrderDetailService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
