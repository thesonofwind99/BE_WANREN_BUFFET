package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAuthService extends UserDetailsService {
    User authenticate(String username);
}
