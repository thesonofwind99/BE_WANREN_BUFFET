package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Sử dụng dịch vụ mặc định để tải người dùng
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // Lấy thông tin từ OAuth2User
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");
        String sub = oAuth2User.getAttribute("sub"); // Unique ID từ Google

        if (email == null || sub == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_email_or_sub"), "Email or sub not found from OAuth2 provider");
        }

        // Kiểm tra xem khách hàng đã tồn tại chưa
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        Customer customer;

        if (!optionalCustomer.isPresent()) {
            // Tạo mới khách hàng
            customer = new Customer();
            customer.setEmail(email);
            customer.setFullName(name);
            customer.setAccountStatus(true);
            customer.setProvider("google");
            customer.setProviderId(sub);
            customerRepository.save(customer);
        } else {
            // Lấy khách hàng hiện có và cập nhật thông tin nếu cần
            customer = optionalCustomer.get();
            customer.setFullName(name);
            customerRepository.save(customer);
        }

        return oAuth2User;
    }
}
