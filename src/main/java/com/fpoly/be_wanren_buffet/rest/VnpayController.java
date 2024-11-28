package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dao.OrderRepository;
import com.fpoly.be_wanren_buffet.dao.PaymentRepository;
import com.fpoly.be_wanren_buffet.dao.TableRepository;
import com.fpoly.be_wanren_buffet.dao.UserRepository;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.entity.Payment;
import com.fpoly.be_wanren_buffet.entity.Tablee;
import com.fpoly.be_wanren_buffet.entity.User;
import com.fpoly.be_wanren_buffet.enums.OrderStatus;
import com.fpoly.be_wanren_buffet.enums.PaymentMethod;
import com.fpoly.be_wanren_buffet.enums.TableStatus;
import com.fpoly.be_wanren_buffet.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/payment")
public class VnpayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/RollBack_VNPAY")
    public String backString(
            @RequestParam("vnp_TxnRef") String txnRef,
            @RequestParam("vnp_TransactionStatus") String statusPayment,
            @RequestParam("vnp_OrderInfo") String orderInfor,
            @RequestParam("vnp_Amount") String amount) {
        System.out.println(orderInfor);
        if (orderInfor.contains("Thanh toan don hang")) {
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
                    return "redirect:http://localhost:3000/checkout/sucessful";
                } else {
                    Long orderId = Long.parseLong(txnRef);
                    orderService.deleteOrderById(orderId - 1);
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
        } else if (orderInfor.contains("Pay for the bill at the table by")) {
            try {
                if ("00".equals(statusPayment)) {
                    //Get userId and orderId from orderInfor of VNPay
                    String[] parts = orderInfor.split(" ");
                    Long userId = Long.parseLong(parts[parts.length - 2]); // Phần tử áp chót
                    Long orderId = Long.parseLong(parts[parts.length - 1]); // Phần tử cuối

                    //Find User by userId
                    User user = userRepository.findById(userId).get();

                    //Find Order by orderId
                    Order order = orderRepository.findById(orderId).get();

                    //Update status of Order
                    order.setOrderStatus(OrderStatus.DELIVERED);

                    //Update amount of Order
                    double totalAmount = Double.parseDouble(amount) / 100;
                    order.setTotalAmount(totalAmount);

                    orderRepository.save(order);

                    //Create Payment with User and Order
                    Payment payment = new Payment();
                    payment.setUser(user);
                    payment.setOrder(order);
                    payment.setPaymentMethod(PaymentMethod.BANK_CARD);
                    payment.setCreatedDate(LocalDateTime.now());
                    payment.setPaymentStatus(true);
                    payment.setAmountPaid(totalAmount);
                    paymentRepository.save(payment);

                    //Find Table by Order
                    Tablee tablee = orderRepository.findById(orderId).get().getTablee();
                    tablee.setTableStatus(TableStatus.EMPTY_TABLE);
                    tableRepository.save(tablee);

                    // Encode the success message
                    String successMessage = URLEncoder.encode("Giao dịch thành công", StandardCharsets.UTF_8);

                    // Redirect to the frontend with a success message
                    return "redirect:http://localhost:3000/checkout/sucessful";
                } else {
                    // Encode the error message
                    String errorMessage = URLEncoder.encode("Giao dịch thất bại", StandardCharsets.UTF_8);
                    // Redirect to the frontend with an error message
                    return "redirect:http://localhost:3000/checkout/failed";
                }
            } catch (NumberFormatException e) {
                // Handle invalid txnRef format
                String errorMessage = URLEncoder.encode("Mã đơn hàng không hợp lệ", StandardCharsets.UTF_8);
                return "redirect:http://localhost:3000/checkout/step3=" + errorMessage;
            } catch (Exception e) {
                // Handle other unforeseen exceptions
                System.out.println(e.getMessage());
                String exceptionMessage = URLEncoder.encode("Lỗi hệ thống", StandardCharsets.UTF_8);
                return "redirect:http://localhost:3000/checkout/step3=" + exceptionMessage;
            }
        }
        return "";

    }

}
