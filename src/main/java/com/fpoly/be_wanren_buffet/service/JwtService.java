package com.fpoly.be_wanren_buffet.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long JWT_EXPIRATION;

    // Phương thức chung để tạo token với các claims bổ sung
    public String generateToken(UserDetails userDetails, Map<String, Object> additionalClaims) {
        return createToken(additionalClaims, userDetails.getUsername());
    }

    // Phương thức tạo token cho Customer
    public String generateTokenForCustomer(UserDetails userDetails, String fullName, String email, String phone , Long UserId , String address) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("fullName", fullName);
        claims.put("email", email);
        claims.put("phone", phone);
        claims.put("userId", String.valueOf(UserId));
        claims.put("address",address);
        claims.put("roles", Collections.singletonList("CUSTOMER"));
        return generateToken(userDetails, claims);
    }

    // Phương thức tạo token cho User
    public String generateTokenForUser(UserDetails userDetails, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        return generateToken(userDetails, claims);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Set<String> extractRoles(String token) {
        return new HashSet<>(extractAllClaims(token).get("roles", List.class));
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}

