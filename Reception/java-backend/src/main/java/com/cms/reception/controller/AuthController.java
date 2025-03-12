package com.cms.reception.controller;

import com.cms.reception.common.Result;
import com.cms.reception.entity.User;
import com.cms.reception.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            return Result.success(registeredUser);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginForm) {
        try {
            String username = loginForm.get("username");
            String password = loginForm.get("password");
            
            String token = userService.login(username, password);
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public Result<User> getUserInfo(@RequestAttribute Long userId) {
        try {
            return Result.success(userService.getUserInfo(userId));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/verify-code")
    public Result<Void> sendVerifyCode(@RequestBody Map<String, String> params) {
        try {
            String username = params.get("username");
            userService.sendVerifyCode(username);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@RequestBody Map<String, String> params) {
        try {
            String username = params.get("username");
            String code = params.get("code");
            String newPassword = params.get("newPassword");
            userService.resetPassword(username, code, newPassword);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 