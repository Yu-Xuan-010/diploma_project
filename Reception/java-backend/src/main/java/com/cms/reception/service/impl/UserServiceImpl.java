package com.cms.reception.service.impl;

import com.cms.reception.entity.User;
import com.cms.reception.repository.UserRepository;
import com.cms.reception.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        // 1. 根据用户名查询数据库
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false; // 用户不存在
        }

        // 2. 比对加密后的密码
        return passwordEncoder.matches(password, user.getPassword()); // 验证密码
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        // 验证邮箱唯一性
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

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }
} 