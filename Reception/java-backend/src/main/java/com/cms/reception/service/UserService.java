package com.cms.reception.service;

import com.cms.reception.common.Result;
import com.cms.reception.entity.User;
import com.cms.reception.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean validateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return true;  // 验证成功
        }
        return false;  // 验证失败
    }
}
