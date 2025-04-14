package com.cms.reception.service;

import com.cms.reception.entity.User;
import com.cms.reception.dto.TeacherApplicationDTO;
import com.cms.reception.entity.TeacherApplication;
import com.cms.reception.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface UserService {
    User register(User user);
    User findByUsername(String username);
    User findById(Long id);
    boolean validateUser(String username, String password);
    User updateUser(User user);
    boolean changePassword(String username, String oldPassword, String newPassword);
    void updatePassword(Long userId, String newPassword);
    boolean verifyPassword(User user, String rawPassword);
    void updateStatus(Long userId, String status);
    void updateLoginInfo(Long userId, Integer loginAttempts, Date lastLogin);
    TeacherApplication submitTeacherApplication(TeacherApplicationDTO applicationDTO);
    TeacherApplication getTeacherApplication(Long userId);
    // UserService.java
    User getUserByUsername(String username);
}
