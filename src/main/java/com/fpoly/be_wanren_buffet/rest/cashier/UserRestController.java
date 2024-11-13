package com.fpoly.be_wanren_buffet.rest.cashier;


import com.fpoly.be_wanren_buffet.dao.UserRepository;
import com.fpoly.be_wanren_buffet.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/User")
public class UserRestController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/findByUsername")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
