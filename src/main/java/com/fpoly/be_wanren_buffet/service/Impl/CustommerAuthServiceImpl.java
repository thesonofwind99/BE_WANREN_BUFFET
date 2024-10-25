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
public class CustommerAuthServiceImpl implements CustomerAuthService {
    @Autowired
    CustomerRepository customerRepository;



    @Override
    public Customer authenticate(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username);
        System.out.println(customer);
        if (customer == null) {
            throw new UsernameNotFoundException("Customer not found with username: " + username);
        }
        return User.builder()
                .username(customer.getUsername())
                .password(customer.getPassword())
                .authorities("CUSTOMER")
                .accountLocked(!customer.getAccountStatus())
                .build();
    }

}
