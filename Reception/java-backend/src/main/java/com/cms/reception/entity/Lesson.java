package com.cms.reception.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Lesson {
    private Long id;
    private Long courseId;
    private String title;
    private String description;
    private String videoUrl;
    private Integer duration;
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 