package com.cms.reception.service.impl;

import com.cms.reception.entity.User;
import com.cms.reception.entity.TeacherApplication;
import com.cms.reception.entity.ApplicationStatus;
import com.cms.reception.dto.TeacherApplicationDTO;
import com.cms.reception.mapper.UserMapper;
import com.cms.reception.repository.UserRepository;
import com.cms.reception.repository.TeacherApplicationRepository;
import com.cms.reception.repository.MajorRepository;
import com.cms.reception.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeacherApplicationRepository teacherApplicationRepository;
    private final MajorRepository majorRepository;
    private final UserMapper userMapper;

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
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
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
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 检查邮箱是否已被其他用户使用
        User userWithEmail = userRepository.findByEmail(user.getEmail());
        if (userWithEmail != null && !userWithEmail.getId().equals(user.getId())) {
            throw new RuntimeException("Email already exists");
        }

        // 保留原有的关联关系
        if (user.getMajorId() != null) {
            existingUser.setMajorId(user.getMajorId());
            
            // 根据专业ID自动设置学院ID
            majorRepository.findById(user.getMajorId()).ifPresent(major -> {
                existingUser.setCollegeId(major.getCollegeId());
            });
        }
        
        if (user.getCollegeId() != null) {
            existingUser.setCollegeId(user.getCollegeId());
        }

        // 更新其他字段
        existingUser.setNickname(user.getNickname());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setRealName(user.getRealName());
        existingUser.setGender(user.getGender());
        existingUser.setBirthday(user.getBirthday());
        existingUser.setAddress(user.getAddress());
        existingUser.setAvatar(user.getAvatar());

        // 设置更新时间为LocalDateTime类型
        existingUser.setUpdateTime(LocalDateTime.now());
        
        // 更新用户信息
        return userRepository.save(existingUser);
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
    public void updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        // 加密新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean verifyPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    @Transactional
    public void updateStatus(Long userId, String status) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateLoginInfo(Long userId, Integer loginAttempts, Date lastLogin) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        user.setLoginAttempts(loginAttempts);
        user.setLastLogin(lastLogin);
        userRepository.save(user);
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
        if (teacherApplicationRepository.existsByUserIdAndStatus(applicationDTO.getUserId(), TeacherApplication.ApplicationStatus.PENDING)) {
            throw new IllegalStateException("您已经有一个待审核的申请");
        }

        // 创建新的申请
        TeacherApplication application = new TeacherApplication();
        application.setUserId(applicationDTO.getUserId());
        application.setReason(applicationDTO.getReason());
        application.setExpertise(String.join(",", applicationDTO.getExpertise()));
        application.setExperience(applicationDTO.getExperience());
        application.setStatus(TeacherApplication.ApplicationStatus.PENDING);

        return teacherApplicationRepository.save(application);
    }

    @Override
    public TeacherApplication getTeacherApplication(Long userId) {
        return teacherApplicationRepository.findFirstByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException("未找到申请记录"));
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
} 