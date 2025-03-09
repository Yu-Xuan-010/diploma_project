package com.ruoyi.web.controller.sen.domain;

import lombok.Data;
import java.util.Date;

@Data
public class CoursePopularityDTO {
    private Long courseId;
    private String courseName;
    private Integer viewCount;
    private Integer studentCount;
    private Date createdAt;
} 