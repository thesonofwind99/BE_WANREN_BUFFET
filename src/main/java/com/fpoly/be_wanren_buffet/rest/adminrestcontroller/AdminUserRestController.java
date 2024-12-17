package com.fpoly.be_wanren_buffet.rest.adminrestcontroller;

import com.fpoly.be_wanren_buffet.dao.UserRepository;
import com.fpoly.be_wanren_buffet.dao.WorkScheduleRepository;
import com.fpoly.be_wanren_buffet.dao.WorkShiftRepository;
import com.fpoly.be_wanren_buffet.entity.User;
import com.fpoly.be_wanren_buffet.entity.WorkSchedule;
import com.fpoly.be_wanren_buffet.entity.WorkShift;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@RestController
@RequestMapping("/User")
public class AdminUserRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkScheduleRepository workScheduleRepository;
    @Autowired
    private WorkShiftRepository workShiftRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        // Kiểm tra trùng username
        Optional<User> existingUserByUsername = userRepository.findByUsername(user.getUsername());
        if (existingUserByUsername.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists. Please choose another username.");
        }

        // Kiểm tra trùng email
        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserByEmail.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists. Please choose another email.");
        }

        try {
            // Lưu user mới
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);

            // Lấy shift mặc định (ví dụ: shift ID = 1)
            // Kiểm tra xem defaultShift có tồn tại không, nếu không thì tạo mới
            WorkShift defaultShift = workShiftRepository.findById(0L)
                    .orElseGet(() -> {
                        WorkShift newDefaultShift = new WorkShift();
                        newDefaultShift.setShiftId(0L);
                        newDefaultShift.setShiftName("Default Shift");
                        newDefaultShift.setStartTime(LocalTime.of(0, 0)); // 00:00
                        newDefaultShift.setEndTime(LocalTime.of(0, 0));   // 00:00
                        return workShiftRepository.save(newDefaultShift);
                    });

// Tạo dữ liệu WorkSchedule
            WorkSchedule workSchedule = new WorkSchedule();
            workSchedule.setWorkDate(new Date()); // Gán ngày làm việc mặc định
            workSchedule.setShift(defaultShift); // Gán shift mặc định
            workSchedule.setUser(savedUser);     // Gán user vừa được lưu

            workScheduleRepository.save(workSchedule);

            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating user and associated work schedule: " + e.getMessage());
        }
    }



    @PatchMapping("/update/{id}")
    public ResponseEntity<Object> updatePartialUser(@PathVariable Long id, @RequestBody User user) {
        // Tìm user hiện tại theo ID
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        // Kiểm tra trùng lặp username và email trước khi cập nhật
        if (isFieldChangedAndExists(user.getUsername(), existingUser.getUsername(), userRepository::findByUsername)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists. Please choose another username.");
        }
        if (isFieldChangedAndExists(user.getEmail(), existingUser.getEmail(), userRepository::findByEmail)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists. Please choose another email.");
        }

        // Cập nhật các trường không null
        updateFields(user, existingUser);

        // Kiểm tra nếu mật khẩu có thay đổi thì mới mã hóa lại
        if (user.getPassword() != null && !user.getPassword().equals(existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Lưu lại người dùng đã cập nhật
        return ResponseEntity.ok(userRepository.save(existingUser));
    }


    // Phương thức kiểm tra nếu trường đã thay đổi và trùng với user khác
    private boolean isFieldChangedAndExists(String newValue, String currentValue, Function<String, Optional<User>> finderFunction) {
        return newValue != null && !newValue.equals(currentValue) && finderFunction.apply(newValue).isPresent();
    }

    // Phương thức cập nhật các trường không null
    private void updateFields(User source, User target) {
        Optional.ofNullable(source.getUsername()).ifPresent(target::setUsername);
        Optional.ofNullable(source.getEmail()).ifPresent(target::setEmail);
        Optional.ofNullable(source.getFullName()).ifPresent(target::setFullName);
        Optional.ofNullable(source.getPhoneNumber()).ifPresent(target::setPhoneNumber);
        Optional.ofNullable(source.getAddress()).ifPresent(target::setAddress);
        Optional.ofNullable(source.getUserType()).ifPresent(target::setUserType);
        Optional.ofNullable(source.getAccountStatus()).ifPresent(target::setAccountStatus);
        target.setUpdatedDate(LocalDateTime.now());
    }
    @PatchMapping("/updateAccountStatus/{id}")
    public ResponseEntity<Object> updateAccountStatus(@PathVariable Long id, @RequestBody boolean accountStatus) {
        // Tìm user theo ID
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User existingUser = existingUserOptional.get();

        // Cập nhật trường accountStatus
        existingUser.setAccountStatus(accountStatus);
        existingUser.setUpdatedDate(LocalDateTime.now());

        // Lưu thay đổi vào database
        User savedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(savedUser);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        // Delete user if found
        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
    }



}
