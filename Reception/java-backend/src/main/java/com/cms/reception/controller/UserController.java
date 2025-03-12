package com.cms.reception.controller;

import com.cms.reception.common.Result;
import com.cms.reception.model.User;
import com.cms.reception.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 用户登录
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        return userService.login(user);
    }

    // 用户注册
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        return userService.register(user);
    }

    // 获取用户信息
    @GetMapping("/profile")
    public Result getProfile() {
        return userService.getProfile();
    }

    // 更新用户信息
    @PutMapping("/profile")
    public Result updateProfile(@RequestBody User user) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.updateProfile(user);
    }

    // 重置密码
    @PostMapping("/reset-password")
    public Result resetPassword(@RequestParam String username, @RequestParam String code, @RequestParam String newPassword) {
        return userService.resetPassword(username, code, newPassword);
    }

    // 发送验证码
    @PostMapping("/send-code")
    public Result sendCode(@RequestParam String username) {
        return userService.sendCode(username);
    }
} 