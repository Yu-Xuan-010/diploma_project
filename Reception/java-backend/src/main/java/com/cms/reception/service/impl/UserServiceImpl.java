package com.cms.reception.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cms.reception.entity.User;
import com.cms.reception.mapper.UserMapper;
import com.cms.reception.service.UserService;
import com.cms.reception.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional
    public User register(User user) {
        // 检查用户名是否已存在
        if (checkUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("ACTIVE");
        user.setRole("STUDENT");
        user.setLoginAttempts(0);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        // 保存用户
        userMapper.insert(user);
        return user;
    }

    @Override
    public String login(String username, String password) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        if ("INACTIVE".equals(user.getStatus())) {
            throw new RuntimeException("账号已被禁用");
        }

        // 生成token
        return jwtUtil.generateToken(user.getId());
    }

    @Override
    public User getUserInfo(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public boolean checkUsername(String username) {
        return userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)) > 0;
    }

    @Override
    public void sendVerifyCode(String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 生成6位随机验证码
        String code = String.format("%06d", new Random().nextInt(1000000));
        
        // TODO: 发送验证码到用户邮箱或手机
        // 这里暂时只打印验证码
        System.out.println("验证码: " + code);
        
        // 将验证码保存到用户信息中
        user.setVerifyCode(code);
        user.setVerifyCodeExpireTime(new Date(System.currentTimeMillis() + 5 * 60 * 1000)); // 5分钟有效期
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void resetPassword(String username, String code, String newPassword) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (user.getVerifyCode() == null || !user.getVerifyCode().equals(code)) {
            throw new RuntimeException("验证码错误");
        }

        if (user.getVerifyCodeExpireTime() == null || user.getVerifyCodeExpireTime().before(new Date())) {
            throw new RuntimeException("验证码已过期");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVerifyCode(null);
        user.setVerifyCodeExpireTime(null);
        user.setUpdatedAt(new Date());
        userMapper.updateById(user);
    }
}