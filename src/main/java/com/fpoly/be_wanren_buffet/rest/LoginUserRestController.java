// LoginUserRestController.java

package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dao.RoleRepository;
import com.fpoly.be_wanren_buffet.dao.UserRepository;
import com.fpoly.be_wanren_buffet.entity.User;
import com.fpoly.be_wanren_buffet.entity.UserRole;
import com.fpoly.be_wanren_buffet.security.JwtResponse;
import com.fpoly.be_wanren_buffet.security.LoginRequest;
import com.fpoly.be_wanren_buffet.service.JwtService;
import org.apache.catalina.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class LoginUserRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    RoleRepository roleRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginRequest) {
        try {
            // Xác thực người dùng
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // Kiểm tra xem xác thực thành công hay không
            if (authentication.isAuthenticated()) {
                // Lấy thông tin người dùng đã đăng nhập
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                // Lấy thông tin bổ sung của người dùng
                Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
                if (!userOpt.isPresent()) {
                    return ResponseEntity.status(401).body("Đăng nhập thất bại: Người dùng không tồn tại");
                }

                User authenticatedUser = userOpt.get();

                List<String> roles = roleRepository.findRoleNamesByUserId(authenticatedUser.getUserId());




                // Tạo JWT
                final String jwt = jwtService.generateTokenForUser(userDetails, roles);

                return ResponseEntity.ok(new JwtResponse(jwt));
            } else {
                return ResponseEntity.status(401).body("Đăng nhập thất bại");
            }
        } catch (BadCredentialsException e) {
            // Xử lý sai thông tin đăng nhập
            return ResponseEntity.status(401).body("Đăng nhập thất bại: Sai tên đăng nhập hoặc mật khẩu");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Đã xảy ra lỗi khi đăng nhập");
        }
    }
}
