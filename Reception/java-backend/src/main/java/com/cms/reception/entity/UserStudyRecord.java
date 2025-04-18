package com.cms.reception.entity;

import lombok.Data;
import java.util.Date;

@Data
public class UserStudyRecord {
    private Long id;
    private Long userId;
    private Long courseId;
    private Long lessonId;
    private Integer duration;  // 学习时长（秒）
    private Date studyDate;    // 学习日期
    private Date createTime;   // 创建时间
    private Date updateTime;   // 更新时间
    private Integer status;    // 状态：0-未完成，1-已完成
    
    // 扩展字段（用于显示）
    private String lessonTitle;
    private String courseName;
    private String teacherName;
} 