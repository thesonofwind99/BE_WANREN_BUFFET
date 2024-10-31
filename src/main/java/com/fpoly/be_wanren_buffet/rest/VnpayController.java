package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.entity.Order;
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

    @GetMapping("/RollBack_VNPAY")
    public String backString(
            @RequestParam("vnp_TxnRef") String txnRef,
            @RequestParam("vnp_TransactionStatus") String statusPayment) {
        System.out.println("00".equals(txnRef));
        try {
            if ("00".equals(statusPayment)) {
                // Parse the transaction reference to a Long
                Long orderId = Long.parseLong(txnRef);
                // Retrieve and update the order status
                Order order = orderService.GetOrderById(orderId - 1);
                order.setOrderStatus(OrderStatus.PREPARING_ORDER);
                orderService.UpdateStatusOrder(order);

                // Encode the success message
                String successMessage = URLEncoder.encode("Giao dịch thành công", StandardCharsets.UTF_8);

                // Redirect to the frontend with a success message
                return "redirect:http://localhost:3000/checkout?success=" + successMessage;
            } else {
                Long orderId = Long.parseLong(txnRef);
                orderService.deleteOrderById(orderId -1);
                // Encode the error message
                String errorMessage = URLEncoder.encode("Giao dịch thất bại", StandardCharsets.UTF_8);

                // Redirect to the frontend with an error message
                return "redirect:http://localhost:3000/checkout?error=" + errorMessage;
            }
        } catch (NumberFormatException e) {
            // Handle invalid txnRef format
            String errorMessage = URLEncoder.encode("Mã đơn hàng không hợp lệ", StandardCharsets.UTF_8);
            return "redirect:http://localhost:3000/checkout?error=" + errorMessage;
        } catch (Exception e) {
            // Handle other unforeseen exceptions
            System.out.println(e.getMessage());
            String exceptionMessage = URLEncoder.encode("Lỗi hệ thống", StandardCharsets.UTF_8);
            return "redirect:http://localhost:3000/checkout?error=" + exceptionMessage;
        }
    }
}
