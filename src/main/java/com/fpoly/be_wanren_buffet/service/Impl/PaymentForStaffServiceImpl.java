package com.fpoly.be_wanren_buffet.service.Impl;

import com.fpoly.be_wanren_buffet.converter.PaymentConvert;
import com.fpoly.be_wanren_buffet.dao.OrderRepository;
import com.fpoly.be_wanren_buffet.dao.PaymentRepository;
import com.fpoly.be_wanren_buffet.dao.UserRepository;
import com.fpoly.be_wanren_buffet.dto.request.PaymentForStaffRequest;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.entity.Payment;
import com.fpoly.be_wanren_buffet.entity.User;
import com.fpoly.be_wanren_buffet.service.PaymentForStaffService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@AllArgsConstructor
public class PaymentForStaffServiceImpl implements PaymentForStaffService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    @Override
    public Long createPaymentForStaff(PaymentForStaffRequest paymentForStaffRequest) {
        Payment payment = PaymentConvert.convertToPayment(paymentForStaffRequest);
        Order order = orderRepository.findById(paymentForStaffRequest.getOrderId()).get();
        User user = userRepository.findById(paymentForStaffRequest.getUserId()).get();
        payment.setUser(user);
        payment.setOrder(order);
        payment.setCreatedDate(LocalDateTime.now());
        Payment payment1 = paymentRepository.save(payment);
        return payment1.getPaymentId();
    }
}
