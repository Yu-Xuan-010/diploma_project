package com.sen.web.controller.sen.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.sen.common.annotation.Excel;
import com.sen.common.core.domain.BaseEntity;

/**
 * 用户对象 user
 * 
 * @author sen
 * @date 2025-03-06
 */
public class UserList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户名 */
    @Excel(name = "用户名")
    private String username;

    /** 密码 */
    private String password;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 头像 */
    private String avatar;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 用户角色 */
    @Excel(name = "用户角色")
    private String role;

    /** 电话号码 */
    @Excel(name = "电话号码")
    private String phoneNumber;

    /** 真实姓名 */
    private String realName;

    /** 用户状态 */
    @Excel(name = "用户状态")
    private String status;

    /** 最后登录 */
    private Date lastLogin;

    /** 登录次数 */
    private Long loginAttempts;

    /** 性别 */
    private String gender;

    /** 出生日期 */
    private Date birthday;

    /** 地址 */
    private String address;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
    }
    public void setCreatedAt(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getCreatedAt() 
    {
        return createTime;
    }
    public void setUpdatedAt(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public Date getUpdatedAt() 
    {
        return updateTime;
    }
    public void setRole(String role) 
    {
        this.role = role;
    }

    public String getRole() 
    {
        return role;
    }
    public void setPhoneNumber(String phoneNumber) 
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() 
    {
        return phoneNumber;
    }
    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setLastLogin(Date lastLogin) 
    {
        this.lastLogin = lastLogin;
    }

    public Date getLastLogin() 
    {
        return lastLogin;
    }
    public void setLoginAttempts(Long loginAttempts) 
    {
        this.loginAttempts = loginAttempts;
    }

    public Long getLoginAttempts() 
    {
        return loginAttempts;
    }
    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getGender() 
    {
        return gender;
    }
    public void setBirthday(Date birthday) 
    {
        this.birthday = birthday;
    }

    public Date getBirthday() 
    {
        return birthday;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("email", getEmail())
            .append("avatar", getAvatar())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .append("role", getRole())
            .append("phoneNumber", getPhoneNumber())
            .append("realName", getRealName())
            .append("status", getStatus())
            .append("lastLogin", getLastLogin())
            .append("loginAttempts", getLoginAttempts())
            .append("gender", getGender())
            .append("birthday", getBirthday())
            .append("address", getAddress())
            .toString();
    }
}
