package com.cms.reception.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("lesson")
public class Lesson {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long courseId;
    
    private String title;
    
    private String description;
    
    private String videoUrl;
    
    private Integer duration;
    
    private Integer sort;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    private String status;
    
    // 关联字段
    @TableField(exist = false)
    private String courseName;
} 