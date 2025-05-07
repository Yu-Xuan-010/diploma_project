package com.cms.reception.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * &#064;BelongsProject: diploma_project
 * &#064;BelongsPackage: com.cms.reception.dto
 * &#064;Author: gaogao
 * &#064;CreateTime: 2025-05-07  16:58
 * &#064;Description: TODO
 * &#064;Version: 1.0
 */
@Data
public class StudyRecordDTO {
    private Long id;
    private Long userId;
    private Long lessonId;
    private String lessonTitle;
    private Long courseId;
    private String courseName;
    private Integer totalDuration;
    private LocalDateTime lastStudyTime;
    private Integer status;

    public StudyRecordDTO(Long id, Long userId, Long lessonId, String lessonTitle,
                          Long courseId, String courseName, Integer totalDuration,
                          LocalDateTime lastStudyTime, Integer status) {
        this.id = id;
        this.userId = userId;
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.courseId = courseId;
        this.courseName = courseName;
        this.totalDuration = totalDuration;
        this.lastStudyTime = lastStudyTime;
        this.status = status;
    }


}


