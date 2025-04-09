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
import java.time.LocalDateTime;
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
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(ApiResponse.success(user));
        }
        return ResponseEntity.ok(ApiResponse.error("用户不存在"));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody User updatedUser) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username);
            
            if (currentUser == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }

            // 更新用户信息
            currentUser.setUsername(updatedUser.getUsername());
            currentUser.setEmail(updatedUser.getEmail());
            currentUser.setPhoneNumber(updatedUser.getPhoneNumber());
            currentUser.setAvatar(updatedUser.getAvatar());
            currentUser.setGender(updatedUser.getGender());
            currentUser.setMajorId(updatedUser.getMajorId());
            currentUser.setCollegeId(updatedUser.getCollegeId());
            currentUser.setRealName(updatedUser.getRealName());
            currentUser.setNickname(updatedUser.getNickname());
            currentUser.setBirthday(updatedUser.getBirthday());
            currentUser.setAddress(updatedUser.getAddress());

            userService.updateUser(currentUser);
            return ResponseEntity.ok(ApiResponse.success("个人信息更新成功"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("更新失败：" + e.getMessage()));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            
            if (user == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }

            if (!userService.verifyPassword(user, request.getOldPassword())) {
                return ResponseEntity.ok(ApiResponse.error("原密码错误"));
            }

            userService.updatePassword(user.getId(), request.getNewPassword());
            return ResponseEntity.ok(ApiResponse.success("密码修改成功"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("密码修改失败：" + e.getMessage()));
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

class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
} 