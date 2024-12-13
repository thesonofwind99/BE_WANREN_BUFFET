package com.fpoly.be_wanren_buffet.rest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.dao.OrderRepository;
import com.fpoly.be_wanren_buffet.dao.PaymentRepository;
import com.fpoly.be_wanren_buffet.dao.TableRepository;
import com.fpoly.be_wanren_buffet.dao.UserRepository;
import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.entity.Payment;
import com.fpoly.be_wanren_buffet.entity.Tablee;
import com.fpoly.be_wanren_buffet.entity.User;
import com.fpoly.be_wanren_buffet.enums.OrderStatus;
import com.fpoly.be_wanren_buffet.enums.PaymentMethod;
import com.fpoly.be_wanren_buffet.enums.TableStatus;
import com.fpoly.be_wanren_buffet.service.OrderService;

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

    @Autowired
    private CustomerRepository customerRepository;

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
                    Payment payment = paymentRepository.findByOrderId(orderId - 1);
                    payment.setPaymentStatus(true);
                    paymentRepository.save(payment);
                    // Encode the success message
                    String successMessage = URLEncoder.encode("Giao dịch thành công", StandardCharsets.UTF_8);

                    // Redirect to the frontend with a success message
                    return "redirect:https://wanrenbuffet.netlify.app/checkout?success=" +successMessage;
                } else {
                    Long orderId = Long.parseLong(txnRef);
                    orderService.deleteOrderById(orderId - 1);
                    // Encode the error message
                    String errorMessage = URLEncoder.encode("Giao dịch thất bại", StandardCharsets.UTF_8);

                    // Redirect to the frontend with an error message
                    return "redirect:https://wanrenbuffet.netlify.app/checkout?error=" + errorMessage;
                }
            } catch (NumberFormatException e) {
                // Handle invalid txnRef format
                String errorMessage = URLEncoder.encode("Mã đơn hàng không hợp lệ", StandardCharsets.UTF_8);
                return "redirect:https://wanrenbuffet.netlify.app/checkout?error=" + errorMessage;
            } catch (Exception e) {
                // Handle other unforeseen exceptions
                System.out.println(e.getMessage());
                String exceptionMessage = URLEncoder.encode("Lỗi hệ thống", StandardCharsets.UTF_8);
                return "redirect:https://wanrenbuffet.netlify.app/checkout?error=" + exceptionMessage;
            }
        } else if (orderInfor.contains("Pay for the bill at the table by")) {
            try {
                if ("00".equals(statusPayment)) {

                    String[] parts = orderInfor.split(" "); // Tách chuỗi bằng dấu cách

                    // Tách từng biến từ mảng
                    Long userId = Long.parseLong(parts[parts.length - 4]); // Phần tử thứ 4 từ cuối
                    Long orderId = Long.parseLong(parts[parts.length - 3]); // Phần tử thứ 3 từ cuối
                    // Kiểm tra và lấy giá trị không bắt buộc
                    String phoneNumber = null;
                    Long pointsToDeduct = null;

                    if (parts.length >= 5) {
                        phoneNumber = parts[parts.length - 2].trim(); // Loại bỏ khoảng trắng nếu có
                        if (phoneNumber.isEmpty()) {
                            phoneNumber = null; // Đặt lại là null nếu chuỗi rỗng
                        }
                    }
                    if (parts.length >= 6) {
                        try {
                            pointsToDeduct = Long.parseLong(parts[parts.length - 1]);
                        } catch (NumberFormatException e) {
                            pointsToDeduct = null; // Đặt lại là null nếu không thể parse được
                        }
                    }

                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new Exception("User not found"));

                    Optional<Order> optionalOrder = orderRepository.findById(orderId);
                    if (optionalOrder.isEmpty()) {
                        throw new Exception("Order not found");
                    }

                    Order order = optionalOrder.get();
                    order.setOrderStatus(OrderStatus.DELIVERED);
                    order.setTotalAmount(Double.parseDouble(amount) / 100);
                    orderRepository.save(order);

                    Optional<Payment> optionalPayment = paymentRepository.findByOrder(order);
                    Payment payment;
                    if (optionalPayment.isPresent()) {
                        payment = optionalPayment.get();
                        payment.setPaymentStatus(true);
                        payment.setAmountPaid(Double.parseDouble(amount) / 100);
                        payment.setPaymentMethod(PaymentMethod.VNPAY);
                        payment.setCreatedDate(LocalDateTime.now());
                    } else {
                        payment = new Payment();
                        payment.setUser(user);
                        payment.setOrder(order);
                        payment.setPaymentMethod(PaymentMethod.VNPAY);
                        payment.setCreatedDate(LocalDateTime.now());
                        payment.setPaymentStatus(true);
                        payment.setAmountPaid(Double.parseDouble(amount) / 100);
                    }
                    paymentRepository.save(payment);

                    Tablee tablee = order.getTablee();
                    if (tablee != null) {
                        tablee.setTableStatus(TableStatus.EMPTY_TABLE);
                        tableRepository.save(tablee);
                    }

                    if (phoneNumber != null && pointsToDeduct != null && pointsToDeduct > 0) {
                        Customer customer = customerRepository.findByPhoneNumber(phoneNumber);

                        if (customer.getLoyaltyPoints() < pointsToDeduct) {
                            throw new IllegalArgumentException("Not enough loyalty points to deduct. Current points: "
                                    + customer.getLoyaltyPoints());
                        }

                        // Cập nhật loyalty points
                        customer.setLoyaltyPoints(customer.getLoyaltyPoints() - pointsToDeduct);
                        customerRepository.save(customer);
                    }

                    String successMessage = URLEncoder.encode("Giao dịch thành công", StandardCharsets.UTF_8);
                    return "redirect:https://wanrenbuffet.netlify.app/checkout/sucessful";
                } else {
                    // Handle failed payment status
                    String errorMessage = URLEncoder.encode("Giao dịch thất bại", StandardCharsets.UTF_8);
                    return "redirect:https://wanrenbuffet.netlify.app/checkout/failed";
                }

            } catch (NumberFormatException e) {
                // Handle invalid txnRef format
                String errorMessage = URLEncoder.encode("Mã đơn hàng không hợp lệ", StandardCharsets.UTF_8);
                return "redirect:https://wanrenbuffet.netlify.app/checkout/failed";
            } catch (Exception e) {
                // Handle other unforeseen exceptions
                System.out.println(e.getMessage());
                String exceptionMessage = URLEncoder.encode("Lỗi hệ thống", StandardCharsets.UTF_8);
                return "redirect:https://wanrenbuffet.netlify.app/checkout/checkout/failed";
            }

        }
        return "";

    }

    @GetMapping("/callbck_qrcode/{orderId}")
    public String getCallbackQRCode(@PathVariable("orderId") Long orderId) {
        try {
            // Retrieve and update the order status
            Order order = orderService.GetOrderById(orderId);
            if (order == null) {
                throw new IllegalArgumentException("Order not found for orderId: " + orderId);
            }

            order.setOrderStatus(OrderStatus.PREPARING_ORDER);
            orderService.UpdateStatusOrder(order);

            // Retrieve and update payment status
            Payment payment = paymentRepository.findByOrderId(orderId);
            if (payment == null) {
                throw new IllegalArgumentException("Payment not found for orderId: " + orderId);
            }

            payment.setPaymentStatus(true);
            paymentRepository.save(payment);

            // Encode the success message and include it in the redirect
            String successMessage = URLEncoder.encode("Giao dịch thành công", StandardCharsets.UTF_8);

            // Redirect to the frontend with a success message error
            return "redirect:https://wanrenbuffet.netlify.app/checkout?success=" + successMessage;
        } catch (Exception e) {
            // Handle exceptions and provide a failure message
            String errorMessage = URLEncoder.encode("Có lỗi xảy ra trong quá trình thanh toán", StandardCharsets.UTF_8);
            return "redirect:https://wanrenbuffet.netlify.app/checkout?error=" + errorMessage;
        }
    }

}
