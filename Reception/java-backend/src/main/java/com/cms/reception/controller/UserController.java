package com.cms.reception.controller;

import com.cms.reception.common.Result;
import com.cms.reception.entity.User;
import com.cms.reception.request.LoginRequest;
import com.cms.reception.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        boolean isValidUser = userService.validateUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (isValidUser) {
            return ResponseEntity.ok("登录成功");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
        }
    }
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // 验证 token ...
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("验证成功");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authorization header missing or invalid");
        }
    }

} 