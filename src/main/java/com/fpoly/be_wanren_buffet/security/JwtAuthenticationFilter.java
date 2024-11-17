package com.fpoly.be_wanren_buffet.security;

import com.fpoly.be_wanren_buffet.service.JwtService;
import com.fpoly.be_wanren_buffet.service.CustomerAuthService;
import com.fpoly.be_wanren_buffet.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomerAuthService customerAuthService;

    @Autowired
    private UserAuthService userAuthService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy các vai trò từ token
        Set<String> roles = jwtService.extractRoles(jwt);

        // Thêm dòng in ra vai trò
        System.out.println("Roles from token: " + roles);

        // Hoặc sử dụng logger nếu bạn đã cấu hình
        // log.info("Roles from token: {}", roles);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;
            if (roles.contains("CUSTOMER")) {
                // Xác thực Customer
                userDetails = customerAuthService.loadUserByUsername(username);

            } else {
                // Xác thực User (nhân viên)
                userDetails = userAuthService.loadUserByUsername(username);
            }

            // In ra các quyền (authorities) của userDetails
            System.out.println("Authorities of userDetails: " + userDetails.getAuthorities());

            if (jwtService.isTokenValid(jwt, userDetails) ) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}