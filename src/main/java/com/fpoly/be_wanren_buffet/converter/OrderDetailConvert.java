package com.fpoly.be_wanren_buffet.converter;

import com.fpoly.be_wanren_buffet.dto.request.OrderDetailForStaffRequest;
import com.fpoly.be_wanren_buffet.entity.OrderDetail;

public class OrderDetailConvert {

    public static OrderDetail convertToOrderDetail(OrderDetailForStaffRequest orderDetailRequest) {
        OrderDetail orderDetail = new OrderDetail();

        if(orderDetailRequest.getItemNotes() != null){
            orderDetail.setItemNotes(orderDetailRequest.getItemNotes());
        }

        if(orderDetailRequest.getUnitPrice() != null){
            orderDetail.setUnitPrice(orderDetailRequest.getUnitPrice());
        }

        if(orderDetailRequest.getQuantity() != null){
            orderDetail.setQuantity(orderDetailRequest.getQuantity());
        }

        if(orderDetailRequest.getCreatedDate() != null){
            orderDetail.setCreatedDate(orderDetailRequest.getCreatedDate());
        }

        return orderDetail;
    }
}
