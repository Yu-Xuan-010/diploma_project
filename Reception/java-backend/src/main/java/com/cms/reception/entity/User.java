package com.cms.reception.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String phoneNumber;

    private String realName;

    private String status;

    private Date lastLogin;

    private Integer loginAttempts;

    private String gender;

    private Date birthday;

    private String address;

    private String role;

    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;

    private String verifyCode;

    private Date verifyCodeExpireTime;
}