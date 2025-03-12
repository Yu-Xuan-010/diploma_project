package com.cms.reception.service;

import com.cms.reception.common.Result;
import com.cms.reception.model.User;

public interface UserService {
    Result login(User user);
    Result register(User user);
    Result getProfile();
    Result updateProfile(User user);
    Result resetPassword(String username, String code, String newPassword);
    Result sendCode(String username);
} 