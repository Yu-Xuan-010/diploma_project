package com.cms.reception.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Data
@Entity
public class User implements UserDetails {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 用户ID
    private String username; // 用户名
    private String password; // 密码
    private String email; // 邮箱
    private String avatar; // 头像
    private Date createdAt; // 创建时间
    private Date updatedAt; // 更新时间
    private String role; // 角色 (STUDENT, TEACHER)
    private String phoneNumber; // 电话号码
    private String realName; // 真实姓名
    private String status; // 用户状态 (ACTIVE, INACTIVE, LOCKED)
    private Date lastLogin; // 最后登录时间
    private Integer loginAttempts; // 登录尝试次数
    private String gender; // 性别 (MALE, FEMALE, OTHER)
    private Date birthday; // 出生日期
    private String address; // 地址

    // 实现 UserDetails 接口的方法
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 暂时不处理权限
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 账户未过期
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"LOCKED".equals(status); // 账户未锁定
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 凭证未过期
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equals(status); // 账户已启用
    }
} 