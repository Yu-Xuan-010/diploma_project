package com.cms.reception.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Data
@Entity
@Table(name = "user")
public class User implements UserDetails {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 用户ID

    @Column(unique = true, nullable = false, length = 50)
    private String username; // 用户名

    @Column(nullable = false)
    private String password; // 密码

    @Column(unique = true)
    private String email; // 邮箱

    private String nickname;

    private String avatar; // 头像

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(length = 20)
    private String role; // 角色 (STUDENT, TEACHER)

    @Column(name = "phone_number", length = 20)
    private String phoneNumber; // 电话号码

    @Column(name = "real_name", length = 50)
    private String realName; // 真实姓名

    @Column(length = 20)
    private String status; // 用户状态 (ACTIVE, INACTIVE, LOCKED)

    @Column(name = "last_login")
    private Date lastLogin; // 最后登录时间

    @Column(name = "login_attempts")
    private Integer loginAttempts; // 登录尝试次数

    @Column(length = 10)
    private String gender; // 性别 (MALE, FEMALE, OTHER)

    private Date birthday; // 出生日期

    private String address; // 地址

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
        if (loginAttempts == null) {
            loginAttempts = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    // 实现 UserDetails 接口的方法
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 暂时不处理权限
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true; // 账户未过期
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !"LOCKED".equals(status); // 账户未锁定
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true; // 凭证未过期
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return "ACTIVE".equals(status); // 账户已启用
    }
} 