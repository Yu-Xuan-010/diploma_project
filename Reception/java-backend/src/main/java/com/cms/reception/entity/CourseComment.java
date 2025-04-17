package com.cms.reception.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CourseComment {
    private Long id;
    private Long courseId;
    private Long userId;
    private String content;
    private Integer rating;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 扩展字段，用于显示用户名
    private String userName;
    private String userAvatar;
    
    // 评论回复列表
    private List<CourseCommentReply> replies;
} 