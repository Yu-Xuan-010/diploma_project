package com.cms.reception.service;

import com.cms.reception.entity.User;

public interface UserService {
    User register(User user);
    String login(String username, String password);
    User getUserInfo(Long userId);
    boolean checkUsername(String username);
    void sendVerifyCode(String username);
    void resetPassword(String username, String code, String newPassword);
}