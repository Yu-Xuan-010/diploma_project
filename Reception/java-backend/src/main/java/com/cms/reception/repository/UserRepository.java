package com.cms.reception.repository;

/**
 * &#064;BelongsProject: CMS
 * &#064;BelongsPackage: com.cms.reception.repository
 * &#064;Author: gaogao
 * &#064;CreateTime: 2025-03-12  19:24
 * &#064;Description: TODO
 * &#064;Version: 1.0
 */
import com.cms.reception.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 根据用户名查询用户
    public User findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), username);
        return users.isEmpty() ? null : users.get(0);
    }

    // 根据邮箱查询用户
    public User findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email);
        return users.isEmpty() ? null : users.get(0);
    }

    // 保存用户
    public void save(User user) {
        String sql = "INSERT INTO user (username, password, email, avatar, created_at, updated_at, role, phone_number, real_name, status, last_login, login_attempts, gender, birthday, address, verify_code, verify_code_expire_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getAvatar(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getRole(),
                user.getPhoneNumber(),
                user.getRealName(),
                user.getStatus(),
                user.getLastLogin(),
                user.getLoginAttempts(),
                user.getGender(),
                user.getBirthday(),
                user.getAddress(),
                user.getVerifyCode(),
                user.getVerifyCodeExpireTime());
    }

    // 更新用户
    public void update(User user) {
        String sql = "UPDATE user SET email = ?, avatar = ?, phone_number = ?, real_name = ?, gender = ?, birthday = ?, address = ?, updated_at = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getAvatar(),
                user.getPhoneNumber(),
                user.getRealName(),
                user.getGender(),
                user.getBirthday(),
                user.getAddress(),
                user.getUpdatedAt(),
                user.getId());
    }

    // 更新密码
    public void updatePassword(String username, String newPassword, Date updatedAt) {
        String sql = "UPDATE user SET password = ?, updated_at = ? WHERE username = ?";
        jdbcTemplate.update(sql, newPassword, updatedAt, username);
    }

    // 更新验证码
    public void updateVerifyCode(String username, String verifyCode, Date verifyCodeExpireTime) {
        String sql = "UPDATE user SET verify_code = ?, verify_code_expire_time = ? WHERE username = ?";
        jdbcTemplate.update(sql, verifyCode, verifyCodeExpireTime, username);
    }
}