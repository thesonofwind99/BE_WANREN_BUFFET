package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dto.request.OrderForStaffRequest;
import com.fpoly.be_wanren_buffet.entity.Order;

public interface OrderForStaffService {
    Order addOrder(OrderForStaffRequest orderForStaffRequest);
}
