package com.cms.reception.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Favorite {
    private Long id;
    private Long userId;
    private Long courseId;
    private LocalDateTime createTime;
    
    // 扩展字段，用于显示课程信息
    private String courseName;
    private String courseImage;
    private String teacherName;
} 