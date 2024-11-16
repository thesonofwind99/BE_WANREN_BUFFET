package com.fpoly.be_wanren_buffet.rest.adminrestcontroller;

import com.fpoly.be_wanren_buffet.dao.UserRoleRepository;
import com.fpoly.be_wanren_buffet.entity.User;
import com.fpoly.be_wanren_buffet.entity.UserRole;
import com.fpoly.be_wanren_buffet.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/User")
@RequiredArgsConstructor
public class AdminAccountRestController {

    private final UserRoleRepository userRoleRepository;

    /**
     * API để lấy danh sách tất cả người dùng có vai trò ADMIN.
     */
    @GetMapping("/admins")
    public ResponseEntity<List<UserDTO>> getAllAdminUsers() {
        // Giả sử role_id cho ADMIN là 1
        Long adminRoleId = 1L;

        // Lấy danh sách UserRole liên quan đến role_id = 1
        List<UserRole> adminRoles = userRoleRepository.findByRoleRoleId(adminRoleId);

        // Trích xuất danh sách người dùng từ UserRole và chuyển đổi thành UserDTO
        List<UserDTO> adminUsers = adminRoles.stream()
                .map(userRole -> {
                    User user = userRole.getUser();
                    return new UserDTO(
                            user.getUserId(),
                            user.getUsername(),
                            user.getFullName(),
                            user.getEmail(),
                            user.getPhoneNumber(),
                            user.getAddress(),
                            user.getAccountStatus()
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(adminUsers);
    }
}
