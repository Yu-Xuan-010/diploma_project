package com.cms.reception.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CoursePopularity {
    private Long id;
    private Long courseId;
    private String courseName;
    private Long viewCount;
    private Long studentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 