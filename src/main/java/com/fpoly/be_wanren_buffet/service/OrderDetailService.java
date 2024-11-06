package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dto.OrderDetailRequest;
import com.fpoly.be_wanren_buffet.dto.request.OrderDetailForStaffRequest;
import com.fpoly.be_wanren_buffet.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> addOrderDetail(Long orderId, List<OrderDetailForStaffRequest> orderDetails);
}
