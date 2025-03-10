package com.sen.web.controller.sen.domain;

import lombok.Data;
import java.util.Date;

@Data
public class UserAnalyticsDTO {
    // 用户活跃数据
    private Date date;
    private Integer dau;
    private Integer mau;
    private Integer newUsers;
    
    // 课程学习数据
    private Long courseId;
    private String courseName;
    private String category;
    private Integer studentCount;
    private Double completionRate;
    private Integer views;
    
    // 用户学习偏好
    private String preferenceCategory;
    private Integer userCount;
    private Double percentage;
    
    // 用户学习详情
    private Long userId;
    private String username;
    private String email;
    private Integer totalStudyTime;
    private Integer completedCourses;
    private Date lastStudyTime;
} 