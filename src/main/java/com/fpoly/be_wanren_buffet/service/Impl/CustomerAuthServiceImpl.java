package com.fpoly.be_wanren_buffet.service.Impl;

import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.service.CustomerAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerAuthServiceImpl implements CustomerAuthService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer authenticate(String username) {
        if(username.contains("@")){
            return customerRepository.findByEmail(username).get(); // Sử dụng email thay vì username
        }
        return  customerRepository.findByUsername(username).get();

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm kiếm khách hàng bằng email thay vì username
        Customer customer = authenticate(username);
        if (customer == null) {
            throw new UsernameNotFoundException("Customer not found with email: " + username);
        }

        // In ra thông tin khách hàng để debug
        log.info("Customer loaded: {}", customer);

        // Tạo UserDetails với quyền hạn "CUSTOMER"
        UserDetails userDetails = User.builder()
                .username(customer.getEmail()) // Đảm bảo username là email
                .password(customer.getPassword()) // Sử dụng password nếu cần
                .authorities("CUSTOMER")
                .accountLocked(!customer.getAccountStatus()) // Kiểm tra trạng thái tài khoản
                .build();

        // In ra các quyền của userDetails để debug
        log.info("Authorities of userDetails: {}", userDetails.getAuthorities());

        return userDetails;
    }
}
