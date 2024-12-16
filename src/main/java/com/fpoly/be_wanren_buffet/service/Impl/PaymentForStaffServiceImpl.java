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
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentForStaffServiceImpl implements PaymentForStaffService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    @Override
    public Long createPaymentForStaff(PaymentForStaffRequest paymentForStaffRequest) {
//        Payment payment = PaymentConvert.convertToPayment(paymentForStaffRequest);
//        Order order = orderRepository.findById(paymentForStaffRequest.getOrderId()).get();
//        User user = userRepository.findById(paymentForStaffRequest.getUserId()).get();
//        payment.setUser(user);
//        payment.setOrder(order);
//        payment.setCreatedDate(LocalDateTime.now());
//        Payment payment1 = paymentRepository.save(payment);
//        return payment1.getPaymentId();
        try {
            Payment payment = PaymentConvert.convertToPayment(paymentForStaffRequest);

            Optional<Order> optionalOrder = orderRepository.findById(paymentForStaffRequest.getOrderId());
            if (optionalOrder.isEmpty()) {
                throw new Exception("Order not found");
            }
            Order order = optionalOrder.get();

            Optional<User> optionalUser = userRepository.findById(paymentForStaffRequest.getUserId());
            if (optionalUser.isEmpty()) {
                throw new Exception("User not found");
            }
            User user = optionalUser.get();

            Optional<Payment> existingPayment = paymentRepository.findByOrder(order);

            if (existingPayment.isPresent()) {
                Payment existing = existingPayment.get();
                existing.setUser(user);
                existing.setOrder(order);
                existing.setAmountPaid(payment.getAmountPaid());
                existing.setPaymentMethod(payment.getPaymentMethod());
                existing.setPaymentStatus(payment.getPaymentStatus());
                existing.setCreatedDate(LocalDateTime.now());

                Payment updatedPayment = paymentRepository.save(existing);
                return updatedPayment.getPaymentId();
            } else {
                payment.setUser(user);
                payment.setOrder(order);
                payment.setCreatedDate(LocalDateTime.now());

                Payment newPayment = paymentRepository.save(payment);
                return newPayment.getPaymentId();
            }
        } catch (Exception e) {
            // Handle errors and log if necessary
            System.out.println("Error creating or updating payment: " + e.getMessage());
            throw new RuntimeException("Failed to create or update payment");
        }
    }
}
