package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dto.request.PaymentForStaffRequest;

public interface PaymentForStaffService {
    Long createPaymentForStaff(PaymentForStaffRequest paymentForStaffRequest);
}
