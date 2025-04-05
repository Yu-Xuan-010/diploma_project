package com.cms.reception.controller;

import com.cms.reception.entity.User;
import com.cms.reception.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        if (user != null) {
            // 移除敏感信息
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody User updatedUser) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User existingUser = userService.findByUsername(username);
            
            if (existingUser == null) {
                return ResponseEntity.notFound().build();
            }

            // 更新用户信息，但保持一些字段不变
            existingUser.setNickname(updatedUser.getNickname());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            existingUser.setAvatar(updatedUser.getAvatar());
            existingUser.setGender(updatedUser.getGender());
            existingUser.setBirthday(updatedUser.getBirthday());
            existingUser.setAddress(updatedUser.getAddress());

            User savedUser = userService.updateUser(existingUser);
            savedUser.setPassword(null); // 移除敏感信息
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> passwordChange) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            String oldPassword = passwordChange.get("oldPassword");
            String newPassword = passwordChange.get("newPassword");
            
            if (userService.changePassword(username, oldPassword, newPassword)) {
                return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid old password"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
} 