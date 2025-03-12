package com.cms.reception.service.impl;

import com.cms.reception.common.Result;
import com.cms.reception.model.User;
import com.cms.reception.repository.UserRepository;
import com.cms.reception.service.UserService;
import com.cms.reception.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Result login(User user) {
        User dbUser = userRepository.findByUsername(user.getUsername());
        if (dbUser == null || !passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            return Result.error("用户名或密码错误");
        }
        // 更新最后登录时间
        dbUser.setLastLogin(new Date());
        userRepository.update(dbUser);

        // 生成 JWT
        String token = jwtUtil.generateToken(dbUser.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", dbUser);
        return Result.success(data);
    }

    @Override
    public Result register(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return Result.error("用户名已存在");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return Result.error("邮箱已被注册");
        }
        // 设置默认值
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setStatus("ACTIVE"); // 默认状态为 ACTIVE
        user.setRole("STUDENT"); // 默认角色为 STUDENT
        user.setLoginAttempts(0); // 登录尝试次数初始化为 0
        user.setGender("OTHER"); // 默认性别为 OTHER
        userRepository.save(user);
        return Result.success("注册成功");
    }

    @Override
    public Result getProfile() {
        // 从 SecurityContext 中获取当前用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        return Result.success(user);
    }

    @Override
    public Result updateProfile(User user) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User dbUser = userRepository.findByUsername(username);
        if (dbUser == null) {
            return Result.error("用户不存在");
        }
        // 更新用户信息
        dbUser.setEmail(user.getEmail());
        dbUser.setAvatar(user.getAvatar());
        dbUser.setPhoneNumber(user.getPhoneNumber());
        dbUser.setRealName(user.getRealName());
        dbUser.setGender(user.getGender());
        dbUser.setBirthday(user.getBirthday());
        dbUser.setAddress(user.getAddress());
        dbUser.setUpdatedAt(new Date());
        userRepository.update(dbUser);
        return Result.success("个人信息更新成功");
    }

    @Override
    public Result resetPassword(String username, String code, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        // 验证码校验逻辑（需根据实际需求实现）
        if (!code.equals(user.getVerifyCode()) || new Date().after(user.getVerifyCodeExpireTime())) {
            return Result.error("验证码无效或已过期");
        }
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(new Date());
        userRepository.updatePassword(username, user.getPassword(), user.getUpdatedAt());
        return Result.success("密码重置成功");
    }

    @Override
    public Result sendCode(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        // 生成验证码（示例：6位随机数）
        String code = String.valueOf((int) (Math.random() * 900000 + 100000));
        user.setVerifyCode(code);
        user.setVerifyCodeExpireTime(new Date(System.currentTimeMillis() + 5 * 60 * 1000)); // 5分钟有效
        userRepository.updateVerifyCode(username, code, user.getVerifyCodeExpireTime());
        // 发送验证码（需根据实际需求实现，如通过短信或邮件发送）
        return Result.success("验证码已发送");
    }
} 