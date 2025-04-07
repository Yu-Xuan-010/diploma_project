package com.cms.reception.controller;

import com.cms.reception.dto.TeacherApplicationDTO;
import com.cms.reception.dto.ApiResponse;
import com.cms.reception.entity.TeacherApplication;
import com.cms.reception.entity.User;
import com.cms.reception.service.UserService;
import com.cms.reception.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtUtil.getUsernameFromToken(token);
            if (username != null && jwtUtil.validateToken(token)) {
                User user = userService.findByUsername(username);
                if (user != null) {
                    user.setPassword(null);
                    return ResponseEntity.ok(new ApiResponse(true, "获取用户信息成功", user));
                }
            }
        }
        return ResponseEntity.badRequest().body(new ApiResponse(false, "用户不存在或未登录", null));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody User updatedUser, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                String username = jwtUtil.getUsernameFromToken(token);
                if (username != null && jwtUtil.validateToken(token)) {
                    User existingUser = userService.findByUsername(username);
                    
                    if (existingUser == null) {
                        return ResponseEntity.notFound().build();
                    }

                    // 验证并转换性别值
                    String gender = updatedUser.getGender();
                    if (gender != null) {
                        switch (gender.toLowerCase()) {
                            case "male":
                                gender = "0";
                                break;
                            case "female":
                                gender = "1";
                                break;
                            case "other":
                                gender = "2";
                                break;
                            default:
                                return ResponseEntity.badRequest().body(new ApiResponse(false, "无效的性别值，只能是 male、female 或 other", null));
                        }
                    }

                    existingUser.setNickname(updatedUser.getNickname());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
                    existingUser.setAvatar(updatedUser.getAvatar());
                    existingUser.setGender(gender);
                    existingUser.setBirthday(updatedUser.getBirthday());
                    existingUser.setAddress(updatedUser.getAddress());

                    User savedUser = userService.updateUser(existingUser);
                    savedUser.setPassword(null);
                    return ResponseEntity.ok(new ApiResponse(true, "用户信息更新成功", savedUser));
                }
            }
            return ResponseEntity.badRequest().body(new ApiResponse(false, "用户未登录或 token 无效", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
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
                return ResponseEntity.ok(new ApiResponse(true, "密码修改成功", null));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "旧密码不正确", null));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/applyTeacher")
    public ResponseEntity<?> applyForTeacher(@RequestBody TeacherApplicationDTO application) {
        try {
            userService.submitTeacherApplication(application);
            return ResponseEntity.ok(ApiResponse.success("申请已提交，请等待审核"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }


    @GetMapping("/application/status")
    public ResponseEntity<?> getApplicationStatus(@RequestParam Long userId) {
        try {
            TeacherApplication application = userService.getTeacherApplication(userId);
            return ResponseEntity.ok(new ApiResponse(true, "获取申请状态成功", application));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }
} 