package com.cms.reception.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CourseDTO {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private String image;
    private BigDecimal averageRating;
    private String status;
    private Long teacherId;
    private String teacherName;
    private Integer studentCount;
    private List<LessonDTO> lessons;
} 