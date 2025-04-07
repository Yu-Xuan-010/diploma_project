package com.cms.reception.service;

import com.cms.reception.entity.User;
import com.cms.reception.dto.TeacherApplicationDTO;
import com.cms.reception.entity.TeacherApplication;

public interface UserService {
    User register(User user);
    User findByUsername(String username);

    boolean validateUser(String username, String password);

    User updateUser(User user);

    boolean changePassword(String username, String oldPassword, String newPassword);

    // 教师申请相关方法
    TeacherApplication submitTeacherApplication(TeacherApplicationDTO applicationDTO);
    TeacherApplication getTeacherApplication(Long userId);
}
