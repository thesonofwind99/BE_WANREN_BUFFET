package com.fpoly.be_wanren_buffet.converter;

import com.fpoly.be_wanren_buffet.dto.request.PaymentForStaffRequest;
import com.fpoly.be_wanren_buffet.entity.Payment;
import com.fpoly.be_wanren_buffet.enums.PaymentMethod;

public class PaymentConvert {
    public static Payment convertToPayment(PaymentForStaffRequest paymentForStaffRequest) {
        Payment payment = new Payment();

        if(paymentForStaffRequest.getAmountPaid() != null){
            payment.setAmountPaid(paymentForStaffRequest.getAmountPaid());
        }

        if(paymentForStaffRequest.getPaymentMethod() != null){
            payment.setPaymentMethod(PaymentMethod.valueOf(paymentForStaffRequest.getPaymentMethod()));
        }

        if(paymentForStaffRequest.getPaymentStatus() != null){
            payment.setPaymentStatus(paymentForStaffRequest.getPaymentStatus());
        }

        return payment;
    }
}
