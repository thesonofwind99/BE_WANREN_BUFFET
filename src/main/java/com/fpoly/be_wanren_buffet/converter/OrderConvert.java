package com.fpoly.be_wanren_buffet.converter;

import com.fpoly.be_wanren_buffet.dto.request.OrderForStaffRequest;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.enums.OrderStatus;

public class OrderConvert {
    public static Order convertToOrder(OrderForStaffRequest orderForStaffRequest) {
        Order order = new Order();

        if(orderForStaffRequest.getOrderStatus() != null){
            order.setOrderStatus(OrderStatus.valueOf(orderForStaffRequest.getOrderStatus()));
        }

        if(orderForStaffRequest.getAddress() != null){
            order.setAddress(orderForStaffRequest.getAddress());
        }

        if(orderForStaffRequest.getNotes() != null){
            order.setNotes(orderForStaffRequest.getNotes());
        }

        if(orderForStaffRequest.getTotalAmount() != null){
            order.setTotalAmount(orderForStaffRequest.getTotalAmount());
        }

        if(orderForStaffRequest.getNumberPeople() != null){
            order.setNumberPeople(orderForStaffRequest.getNumberPeople());
        }
        return order;
    }
}
