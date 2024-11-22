package com.fpoly.be_wanren_buffet.service.Impl;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fpoly.be_wanren_buffet.dao.UserRepository;
import com.fpoly.be_wanren_buffet.entity.User;
import com.fpoly.be_wanren_buffet.service.UserAuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserAuthServiceImpl implements UserAuthService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User authenticate(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = authenticate(username);


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAccountStatus(),
                true,
                true,
                true,
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                        .collect(Collectors.toList())
        );
    }
}
