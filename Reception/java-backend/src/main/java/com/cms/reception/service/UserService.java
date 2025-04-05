package com.cms.reception.service;

import com.cms.reception.entity.User;

public interface UserService {
    User register(User user);
    User findByUsername(String username);

    boolean validateUser(String username, String password);

    User updateUser(User user);

    boolean changePassword(String username, String oldPassword, String newPassword);


}
