// PaymentRestController.java
package com.fpoly.be_wanren_buffet.rest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.be_wanren_buffet.config.VnpayConfig;
import com.fpoly.be_wanren_buffet.dao.OrderRepository;
import com.fpoly.be_wanren_buffet.dao.PaymentRepository;
import com.fpoly.be_wanren_buffet.dto.PaymentDTO;
import com.fpoly.be_wanren_buffet.dto.request.PaymentForStaffRequest;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.service.OrderService;
import com.fpoly.be_wanren_buffet.service.PaymentForStaffService;
import com.fpoly.be_wanren_buffet.service.VNPayService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/payment")
public class PaymentRestController {

    @Autowired
    private VnpayConfig vnpayConfig;

    @Autowired
    private VNPayService vnpayService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentForStaffService paymentForStaffService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/create_payment")
    public ResponseEntity<?> getPayment(@RequestParam long price,
            @RequestParam(required = false) String bankCode,
            HttpServletRequest request) {
        List<Order> orders = orderRepository.findAll();
        Order order = orders.get(orders.size() - 1);
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "other";
            long amount = price * 100; // VNPay expects amount in VND * 100
            // Optional: Make bankCode dynamic or configurable
            if (bankCode == null || bankCode.isEmpty()) {
                bankCode = "NCB"; // Default bank code
            }

            String vnp_TxnRef = String.valueOf(order.getOrderId() + 1);
            String vnp_IpAddr = VnpayConfig.getIpAddress(request);

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnpayConfig.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", vnpayConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            // Set creation date
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            // Set expiration date (15 minutes later)
            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            // Optionally set bank code if provided
            if (bankCode != null && !bankCode.isEmpty()) {
                vnp_Params.put("vnp_BankCode", bankCode);
            }

            // Sort the parameters
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            // Build the hash data and query string
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    // Build hash data
                    hashData.append(fieldName).append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                            .append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }

            String queryUrl = query.toString();
            String vnp_SecureHash = VnpayConfig.hmacSHA512(vnpayConfig.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

            String paymentUrl = vnpayConfig.vnp_PayUrl + "?" + queryUrl;
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setStatus("OK");
            paymentDTO.setURL(paymentUrl);
            return ResponseEntity.ok().body(paymentDTO);

        } catch (Exception e) {
            // Log the error (use a logger in real applications)
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while processing the payment.");
        }

    }

    @PostMapping("/create_payment/normal")
    public ResponseEntity<?> createPayment(@RequestBody PaymentForStaffRequest paymentForStaffRequest) {
        Map<String, Object> response = new HashMap<>();
        response.put("payment_id", paymentForStaffService.createPaymentForStaff(paymentForStaffRequest));
        response.put("message", "Tạo giao dịch thành công");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit_order_vnpay")
    public String submidOrder(@RequestParam("amount") Integer orderTotal,
            @RequestParam("orderInfo") String orderInfo) {
        String vnpayUrl = vnpayService.createOrder(orderTotal, orderInfo);
        return vnpayUrl;
    }

}
