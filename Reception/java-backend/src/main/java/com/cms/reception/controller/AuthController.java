package com.cms.reception.controller;

import com.cms.reception.entity.User;
import com.cms.reception.service.UserDetailsService;
import com.cms.reception.service.UserService;
import com.cms.reception.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            logger.info("Attempting to register user: {}", user.getUsername());

            // 基本验证
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username is required"));
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Password is required"));
            }
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
            }

            // 设置默认值
            user.setStatus("ACTIVE");
            user.setLoginAttempts(0);
            if (user.getNickname() == null || user.getNickname().trim().isEmpty()) {
                user.setNickname(user.getUsername());
            }

            // 加密密码并注册用户
            user.setPassword(user.getPassword());
            User registeredUser = userService.register(user);
            logger.info("User registered successfully: {}", registeredUser.getUsername());

            // 认证并生成 JWT 令牌
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);
            String token = jwtUtil.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("userId", registeredUser.getId());
            response.put("username", registeredUser.getUsername());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            logger.error("User registration failed: {}", user.getUsername(), e);
            return ResponseEntity.badRequest().body(Map.of("error", "Username or Email already exists"));
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: {}", user.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        } catch (Exception e) {
            logger.error("Registration failed for user: {}", user.getUsername(), e);
            return ResponseEntity.badRequest().body(Map.of("error", "An unexpected error occurred"));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");
            logger.info("Attempting login for user: {}", username);

            // 执行认证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // 生成 token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            // 获取完整用户信息
            User user = userService.findByUsername(userDetails.getUsername());

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("username", user.getUsername());
            data.put("userId", user.getId());
            data.put("email", user.getEmail());
            data.put("nickname", user.getNickname());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", data);

            logger.info("Login successful for user: {}", username);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "用户名或密码错误"
            ));
        }
    }
} 