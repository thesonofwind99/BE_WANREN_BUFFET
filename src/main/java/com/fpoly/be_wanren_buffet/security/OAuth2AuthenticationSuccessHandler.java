package com.fpoly.be_wanren_buffet.security;

import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.service.CustomerService;
import com.fpoly.be_wanren_buffet.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(JwtService jwtService,
                                              CustomerRepository customerRepository,
                                              CustomerService customerService) {
        this.jwtService = jwtService;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            // Lấy thông tin người dùng từ Google
            String email = oAuth2User.getAttribute("email");
            String fullName = oAuth2User.getAttribute("name");
            String phone = oAuth2User.getAttribute("phone") != null ? oAuth2User.getAttribute("phone") : "";
            String address = oAuth2User.getAttribute("address") != null ? oAuth2User.getAttribute("address") : "";
            String providerId = oAuth2User.getAttribute("sub");

            logger.info("Authenticated user: {}", email);

            // Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu chưa
            Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
            Long userId;



            if (optionalCustomer.isPresent()) {
                // Nếu người dùng đã có tài khoản thông thường (email đã tồn tại)
                Customer existingCustomer = optionalCustomer.get();
                userId = existingCustomer.getCustomerId();
                fullName = existingCustomer.getFullName();
                phone = existingCustomer.getPhoneNumber();
                address = existingCustomer.getAddress();
                email = existingCustomer.getEmail();
                logger.info("User found with email: {}", email);
            } else {
                // Nếu chưa có tài khoản, tạo tài khoản mới
                Customer newCustomer = new Customer();
                newCustomer.setFullName(fullName);
                newCustomer.setEmail(email);
                newCustomer.setPhoneNumber(phone);
                newCustomer.setAddress(address);
                newCustomer.setProvider("google");
                newCustomer.setProviderId(providerId);
                newCustomer.setAccountStatus(true);
                newCustomer.setCustomerType("Khách Mới");
                newCustomer.setUsername(email); // Dùng email làm username
                newCustomer.setPassword("google-authenticated"); // Mật khẩu có thể là mặc định hoặc null

                // Lưu tài khoản mới vào cơ sở dữ liệu
                customerService.register(newCustomer);

                userId = newCustomer.getCustomerId();
                logger.info("Created new user with ID: {}", userId);
            }


            String token = jwtService.generateTokenForCustomer(
                    fullName,
                    email,
                    phone,
                    userId,
                    address
            );

            logger.info("Generated JWT token for user: {}", email);

            // Chuyển hướng người dùng về frontend với token
            response.sendRedirect("http://localhost:3000/login-success?token=" + token);
        } catch (Exception e) {
            logger.error("Error in OAuth2AuthenticationSuccessHandler: {}", e.getMessage());
            response.sendRedirect("http://localhost:3000/login-failure");
        }
    }

}
