package com.cms.reception.service.impl;

import com.cms.reception.entity.User;
import com.cms.reception.entity.TeacherApplication;
import com.cms.reception.entity.ApplicationStatus;
import com.cms.reception.dto.TeacherApplicationDTO;
import com.cms.reception.repository.UserRepository;
import com.cms.reception.repository.TeacherApplicationRepository;
import com.cms.reception.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeacherApplicationRepository teacherApplicationRepository;

    @Override
    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        // 设置默认值
        user.setStatus("ACTIVE");
        user.setLoginAttempts(0);
        user.setRole("STUDENT"); // 默认角色为学生
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean validateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        User userWithEmail = userRepository.findByEmail(user.getEmail());
        if (userWithEmail != null && !userWithEmail.getId().equals(user.getId())) {
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public TeacherApplication submitTeacherApplication(TeacherApplicationDTO applicationDTO) {
        // 检查用户是否存在
        User user = userRepository.findById(applicationDTO.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        // 检查用户是否已经是教师
        if ("TEACHER".equals(user.getRole())) {
            throw new IllegalStateException("您已经是教师身份");
        }

        // 检查是否已有待审核的申请
        if (teacherApplicationRepository.existsByUserIdAndStatus(applicationDTO.getUserId(), ApplicationStatus.PENDING)) {
            throw new IllegalStateException("您已经有一个待审核的申请");
        }

        // 创建新的申请
        TeacherApplication application = new TeacherApplication();
        application.setUserId(applicationDTO.getUserId());
        application.setReason(applicationDTO.getReason());
        application.setExpertise(String.join(",", applicationDTO.getExpertise()));
        application.setExperience(applicationDTO.getExperience());
        application.setStatus(ApplicationStatus.PENDING);

        return teacherApplicationRepository.save(application);
    }

    @Override
    public TeacherApplication getTeacherApplication(Long userId) {
        return teacherApplicationRepository.findByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException("未找到申请记录"));
    }
} 