package com.fpoly.be_wanren_buffet.rest.adminrestcontroller;

import com.fpoly.be_wanren_buffet.dao.UserRepository;
import com.fpoly.be_wanren_buffet.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/User")
public class AdminUserRestController {
    @Autowired
    private UserRepository userRepository;

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

        // Nếu không trùng, lưu user mới
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Object> updatePartialUser(@PathVariable Long id, @RequestBody User partialUpdateUser) {
        // Tìm user hiện tại theo ID
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        // Kiểm tra trùng lặp username và email trước khi cập nhật
        if (isFieldChangedAndExists(partialUpdateUser.getUsername(), existingUser.getUsername(), userRepository::findByUsername)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists. Please choose another username.");
        }
        if (isFieldChangedAndExists(partialUpdateUser.getEmail(), existingUser.getEmail(), userRepository::findByEmail)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists. Please choose another email.");
        }

        // Cập nhật các trường không null và lưu
        updateFields(partialUpdateUser, existingUser);
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
