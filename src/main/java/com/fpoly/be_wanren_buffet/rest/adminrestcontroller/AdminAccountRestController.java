package com.fpoly.be_wanren_buffet.rest.adminrestcontroller;

import com.fpoly.be_wanren_buffet.dao.RoleRepository;
import com.fpoly.be_wanren_buffet.dao.UserRepository;
import com.fpoly.be_wanren_buffet.dao.UserRoleRepository;
import com.fpoly.be_wanren_buffet.entity.Role;
import com.fpoly.be_wanren_buffet.entity.User;
import com.fpoly.be_wanren_buffet.entity.UserRole;
import com.fpoly.be_wanren_buffet.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/User")
@RequiredArgsConstructor
public class AdminAccountRestController {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
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
                            user.getPassword(),
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
    @PostMapping("/admins/create")
    public ResponseEntity<String> createAdminUser(@RequestBody UserDTO userDTO) {
        // Giả sử role_id cho ADMIN là 1
        Long adminRoleId = 1L;

        // Tìm Role ADMIN từ database
        Role adminRole = roleRepository.findById(adminRoleId)
                .orElseThrow(() -> new RuntimeException("Role ADMIN không tồn tại"));

        // Tạo mới User từ thông tin UserDTO
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setFullName(userDTO.getFullName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPhoneNumber(userDTO.getPhoneNumber());
        newUser.setAddress(userDTO.getAddress());
        newUser.setPassword(userDTO.getPassword()); // Lưu ý: Cần mã hóa mật khẩu
        newUser.setAccountStatus(userDTO.getAccountStatus());

        // Lưu User vào database
        userRepository.save(newUser);

        // Tạo UserRole để gắn vai trò ADMIN cho user
        UserRole userRole = new UserRole();
        userRole.setUser(newUser);
        userRole.setRole(adminRole);

        // Lưu UserRole vào database
        userRoleRepository.save(userRole);

        return ResponseEntity.ok("Tài khoản ADMIN đã được tạo thành công");
    }
    @PutMapping("/admins/update/{id}")
    public ResponseEntity<String> updateAdminUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        // Kiểm tra xem User với ID có tồn tại không
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Cập nhật thông tin User từ UserDTO
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setFullName(userDTO.getFullName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        existingUser.setAddress(userDTO.getAddress());
        existingUser.setPassword(userDTO.getPassword()); // Lưu ý: Cần mã hóa mật khẩu
        existingUser.setAccountStatus(userDTO.getAccountStatus());

        // Lưu thông tin đã cập nhật vào database
        userRepository.save(existingUser);

        // Kiểm tra vai trò ADMIN, nếu cần thêm role
        Long adminRoleId = 1L; // ID của role ADMIN
        Role adminRole = roleRepository.findById(adminRoleId)
                .orElseThrow(() -> new RuntimeException("Role ADMIN không tồn tại"));

        // Kiểm tra xem User đã có vai trò ADMIN chưa
        boolean hasAdminRole = userRoleRepository.existsByUserUserIdAndRoleRoleId(id, adminRoleId);

        if (!hasAdminRole) {
            // Nếu chưa có role ADMIN, gắn role ADMIN
            UserRole userRole = new UserRole();
            userRole.setUser(existingUser);
            userRole.setRole(adminRole);
            userRoleRepository.save(userRole);
        }

        return ResponseEntity.ok("Thông tin tài khoản ADMIN đã được cập nhật thành công");
    }

    @DeleteMapping("/admins/delete/{id}")
    public ResponseEntity<String> deleteAdminUser(@PathVariable Long id) {
        // Kiểm tra xem User có tồn tại không
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Xóa tất cả vai trò liên quan đến User
        List<UserRole> userRoles = userRoleRepository.findByUserUserId(id);
        userRoleRepository.deleteAll(userRoles);

        // Xóa User khỏi database
        userRepository.delete(existingUser);

        return ResponseEntity.ok("Tài khoản ADMIN đã được xóa thành công");
    }

}