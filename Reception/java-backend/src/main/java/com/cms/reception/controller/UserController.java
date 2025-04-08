package com.cms.reception.controller;

import com.cms.reception.dto.TeacherApplicationDTO;
import com.cms.reception.dto.ApiResponse;
import com.cms.reception.entity.TeacherApplication;
import com.cms.reception.entity.User;
import com.cms.reception.service.UserService;
import com.cms.reception.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.cms.reception.entity.Major;
import com.cms.reception.entity.College;
import com.cms.reception.service.MajorService;
import com.cms.reception.service.CollegeService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final MajorService majorService;
    private final CollegeService collegeService;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, MajorService majorService, CollegeService collegeService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.majorService = majorService;
        this.collegeService = collegeService;
    }

    @GetMapping("/profile")
    public ApiResponse<User> getUserProfile(Authentication authentication) {
        try {
            log.info("开始获取用户信息，用户名: {}", authentication.getName());
            User user = userService.findByUsername(authentication.getName());
            if (user != null) {
                // 获取专业信息
                Major major = null;
                College college = null;
                if (user.getMajorId() != null) {
                    major = majorService.findById(user.getMajorId());
                    if (major != null) {
                        college = collegeService.findById(major.getCollegeId());
                        log.info("获取到专业信息: {}, 学院信息: {}", major.getName(), college != null ? college.getName() : "null");
                    }
                }

                // 设置专业和学院信息
                if (major != null) {
                    user.setMajor(major);
                }
                if (college != null) {
                    user.setCollege(college);
                    // 设置学院ID，用于前端显示
                    user.setCollegeId(college.getId());
                }

                log.info("成功获取用户信息: {}, 专业: {}, 学院: {}", 
                    user.getUsername(), 
                    user.getMajor() != null ? user.getMajor().getName() : "null",
                    user.getCollege() != null ? user.getCollege().getName() : "null");
                
                return ApiResponse.success(user);
            } else {
                log.warn("未找到用户信息: {}", authentication.getName());
                return ApiResponse.error("用户不存在");
            }
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return ApiResponse.error("获取用户信息失败");
        }
    }

    @PutMapping("/profile")
    public ApiResponse<User> updateUserProfile(@RequestBody User updatedUser, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                String username = jwtUtil.getUsernameFromToken(token);
                if (username != null && jwtUtil.validateToken(token)) {
                    User existingUser = userService.findByUsername(username);
                    
                    if (existingUser == null) {
                        return ApiResponse.error("用户不存在");
                    }

                    // 验证用户名
                    if (updatedUser.getUsername() != null && !updatedUser.getUsername().trim().isEmpty()) {
                        // 检查用户名是否已被其他用户使用
                        User userWithSameUsername = userService.findByUsername(updatedUser.getUsername());
                        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(existingUser.getId())) {
                            return ApiResponse.error("用户名已被使用");
                        }
                        existingUser.setUsername(updatedUser.getUsername().trim());
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
                                return ApiResponse.error("无效的性别值，只能是 male、female 或 other");
                        }
                        existingUser.setGender(gender);
                    }

                    // 验证手机号
                    if (updatedUser.getPhoneNumber() != null) {
                        String phoneNumber = updatedUser.getPhoneNumber().trim();
                        if (!phoneNumber.matches("^1[3-9]\\d{9}$")) {
                            return ApiResponse.error("无效的手机号码格式");
                        }
                        existingUser.setPhoneNumber(phoneNumber);
                    }

                    // 验证邮箱
                    if (updatedUser.getEmail() != null) {
                        String email = updatedUser.getEmail().trim();
                        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                            return ApiResponse.error("无效的邮箱格式");
                        }
                        existingUser.setEmail(email);
                    }

                    // 更新其他字段
                    if (updatedUser.getAvatar() != null) {
                        existingUser.setAvatar(updatedUser.getAvatar());
                    }
                    if (updatedUser.getMajorId() != null) {
                        existingUser.setMajorId(updatedUser.getMajorId());
                    }

                    User savedUser = userService.updateUser(existingUser);
                    savedUser.setPassword(null); // 防止密码泄露

                    return ApiResponse.success("用户信息更新成功", savedUser);
                }
            }
            return ApiResponse.error("用户未登录或 token 无效");
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return ApiResponse.error("更新失败: " + e.getMessage());
        }
    }

    @PutMapping("/password")
    public ApiResponse<Void> changePassword(@RequestBody Map<String, String> passwordChange) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            String oldPassword = passwordChange.get("oldPassword");
            String newPassword = passwordChange.get("newPassword");
            
            if (userService.changePassword(username, oldPassword, newPassword)) {
                return ApiResponse.success("密码修改成功", null);
            } else {
                return ApiResponse.error("旧密码不正确");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/applyTeacher")
    public ApiResponse<Void> applyForTeacher(@RequestBody TeacherApplicationDTO application) {
        try {
            userService.submitTeacherApplication(application);
            return ApiResponse.success("申请已提交，请等待审核", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/application/status")
    public ApiResponse<TeacherApplication> getApplicationStatus(@RequestParam Long userId) {
        try {
            TeacherApplication application = userService.getTeacherApplication(userId);
            return ApiResponse.success("获取申请状态成功", application);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
} 