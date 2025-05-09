package com.cms.reception.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CourseComment {
    private Long id;
    private Long courseId;
    private Long userId;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 非数据库字段
    private String userName;
    private String userAvatar;

    private Long commentId;
} 