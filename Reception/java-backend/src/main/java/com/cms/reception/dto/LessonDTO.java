package com.cms.reception.dto;

import lombok.Data;

@Data
public class LessonDTO {
    private Long id;
    private Long courseId;
    private String courseName;
    private String title;
    private String description;
    private String videoUrl;
    private Integer duration;
    private Integer sort;
    private String status;
} 