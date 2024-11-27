package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dao.PaymentRepository;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.entity.Payment;
import com.fpoly.be_wanren_buffet.enums.OrderStatus;
import com.fpoly.be_wanren_buffet.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/api/payment")
public class VnpayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Handles the rollback callback from VNPAY.
     *
     * @param txnRef           The transaction reference corresponding to the order ID.
     * @param statusPayment    The transaction status code.
     * @return A redirect URL to the frontend with success or error messages.
     */
    @GetMapping("/RollBack_VNPAY")
    public String backString(
            @RequestParam("vnp_TxnRef") String txnRef,
            @RequestParam("vnp_TransactionStatus") String statusPayment) {
        Payment payment = null;
        try {
            // Validate and parse txnRef
            Long orderId = Long.parseLong(txnRef);
            System.out.println(orderId + "HELLO");
            payment = paymentRepository.findByOrderId(orderId-1);

            if (payment == null) {
                // Payment record not found
                System.err.println("Payment record not found for orderId: " + orderId);
                String errorMessage = URLEncoder.encode("Không tìm thấy thanh toán cho đơn hàng này", StandardCharsets.UTF_8);
                return "redirect:http://localhost:3000/checkout?error=" + errorMessage;
            }

            System.out.println(payment.getPaymentMethod() + " HELLO");

            if ("00".equals(statusPayment)) {
                // Successful payment
                payment.setPaymentStatus(true);
                paymentRepository.save(payment);

                // Update order status to PREPARING_ORDER
                Order order = orderService.GetOrderById(orderId - 1);
                order.setOrderStatus(OrderStatus.PREPARING_ORDER);
                orderService.UpdateStatusOrder(order);

                String successMessage = URLEncoder.encode("Giao dịch thành công", StandardCharsets.UTF_8);
                return "redirect:http://localhost:3000/checkout?success=" + successMessage;
            } else {
                // Payment failed
                orderService.deleteOrderById(orderId);
                String errorMessage = URLEncoder.encode("Giao dịch thất bại", StandardCharsets.UTF_8);
                return "redirect:http://localhost:3000/checkout?error=" + errorMessage;
            }
        } catch (NumberFormatException e) {
            // Handle invalid txnRef format
            System.err.println("Invalid txnRef format: " + txnRef);
            String errorMessage = URLEncoder.encode("Mã đơn hàng không hợp lệ", StandardCharsets.UTF_8);
            return "redirect:http://localhost:3000/checkout?error=" + errorMessage;

        } catch (Exception e) {
            // Handle other unforeseen exceptions
            System.err.println("An error occurred: " + e.getMessage());
            String exceptionMessage = URLEncoder.encode("Lỗi hệ thống", StandardCharsets.UTF_8);
            return "redirect:http://localhost:3000/checkout?error=" + exceptionMessage;
        }
    }
}
