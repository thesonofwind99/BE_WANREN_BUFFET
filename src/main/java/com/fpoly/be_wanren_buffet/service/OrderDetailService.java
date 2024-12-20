package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dto.request.OrderDetailForStaffRequest;
import com.fpoly.be_wanren_buffet.dto.request.OrderDetailUpdateDTO;
import com.fpoly.be_wanren_buffet.dto.response.OrderDetailForStaffResponse;
import com.fpoly.be_wanren_buffet.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> addOrderDetail(Long orderId, List<OrderDetailForStaffRequest> orderDetails);

    List<OrderDetail> addOrUpdateOrderDetail(Long orderId, List<OrderDetailForStaffRequest> orderDetails);

    List<OrderDetailForStaffRequest> getOrderDetailsByOrderId(Long orderId);

    List<OrderDetailForStaffResponse> getOrderDetailsWithNameProduct(Long orderId);

    List<OrderDetailForStaffRequest> updateOrderDetails(List<OrderDetailUpdateDTO> updateDTOs);
}
