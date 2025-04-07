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
@RequestMapping("/user")
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
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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
            logger.info("Attempting login for user: {}", loginRequest.get("username"));

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.get("username"),
                    loginRequest.get("password")
                )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            User user = userService.findByUsername(userDetails.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", userDetails.getUsername());
            response.put("userId", user.getId());
            response.put("email", user.getEmail());
            response.put("nickname", user.getNickname());

            logger.info("Login successful for user: {}", loginRequest.get("username"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Login failed for user: {}", loginRequest.get("username"), e);
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Invalid username or password"
            ));
        }
    }
} 