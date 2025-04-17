package com.cms.reception.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CourseCommentReply {
    private Long id;
    private Long commentId;
    private Long userId;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 扩展字段，用于显示用户名
    private String userName;
    private String userAvatar;
} 