// PaymentRestController.java
package com.fpoly.be_wanren_buffet.rest;
import com.fpoly.be_wanren_buffet.config.VnpayConfig;
import com.fpoly.be_wanren_buffet.dto.PaymentDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentRestController {

    @Autowired
    private VnpayConfig vnpayConfig;

    @GetMapping("/create_payment")
    public ResponseEntity<?> getPayment(@RequestParam long price,
                                        @RequestParam(required = false) String bankCode,
                                        HttpServletRequest request) {
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "other";
            long amount = price * 100; // VNPay expects amount in VND * 100
            // Optional: Make bankCode dynamic or configurable
            if (bankCode == null || bankCode.isEmpty()) {
                bankCode = "NCB"; // Default bank code
            }

            String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
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

    @GetMapping("/RollBack_VNPAY")
    public String backString() {
        return "redirect:http://localhost:3000/";
    }
}
