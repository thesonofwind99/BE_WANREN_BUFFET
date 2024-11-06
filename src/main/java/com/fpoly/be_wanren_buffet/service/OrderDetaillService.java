package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dao.OrderDetailRepository;
import com.fpoly.be_wanren_buffet.entity.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetaillService {
    @Autowired
    OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> findByOrderDetailByOrderId(Long OrderID){
        List<OrderDetail> list = orderDetailRepository.findAll();
        List<OrderDetail> examLisiOrderDetail = new ArrayList<>();
        for (OrderDetail orderDetail : list){
            if(orderDetail.getOrder().getOrderId().equals(OrderID)){
                examLisiOrderDetail.add(orderDetail);
            }
        }
        return examLisiOrderDetail;
    }
}
