package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.entity.Customer;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomerAuthService extends UserDetailsService {
    public Customer authenticate(String username);
}
