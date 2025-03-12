package com.cms.reception.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("course")
public class Course {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String description;
    
    private Long categoryId;
    
    private String image;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    private BigDecimal averageRating;
    
    private String status;
    
    private Long teacherId;
    
    private Integer studentCount;
    
    // 关联字段
    @TableField(exist = false)
    private String categoryName;
    
    @TableField(exist = false)
    private String teacherName;
} 