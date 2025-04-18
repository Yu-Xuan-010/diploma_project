package com.cms.reception.entity;

import lombok.Data;
import java.util.Date;

@Data
public class UserStudyRecord {
    private Long id;
    private Long userId;
    private Long courseId;
    private Long lessonId;
    private Integer studyDuration;
    private Date studyDate;
    private Date createTime;
    private Date updateTime;
    
    // 关联字段
    private String courseName;
    private String lessonTitle;
    private String teacherName;
} 