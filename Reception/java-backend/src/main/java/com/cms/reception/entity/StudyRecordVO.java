package com.cms.reception.entity;

import java.time.LocalDateTime;

/**
 * &#064;BelongsProject: diploma_project
 * &#064;BelongsPackage: com.cms.reception.entity
 * &#064;Author: gaogao
 * &#064;CreateTime: 2025-05-07  05:29
 * &#064;Description: TODO
 * &#064;Version: 1.0
 */
public class StudyRecordVO {
    private Long id;
    private String courseName;
    private String lessonTitle;
    private Integer totalDuration;
    private LocalDateTime lastStudyTime;
    private Integer status;

    public StudyRecordVO(Long id, String courseName, String lessonTitle,
                         Integer totalDuration, LocalDateTime lastStudyTime, Integer status) {
        this.id = id;
        this.courseName = courseName;
        this.lessonTitle = lessonTitle;
        this.totalDuration = totalDuration;
        this.lastStudyTime = lastStudyTime;
        this.status = status;
    }

    public Long id() {
        return id;
    }

    public StudyRecordVO setId(Long id) {
        this.id = id;
        return this;
    }

    public String courseName() {
        return courseName;
    }

    public StudyRecordVO setCourseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public String lessonTitle() {
        return lessonTitle;
    }

    public StudyRecordVO setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
        return this;
    }

    public Integer totalDuration() {
        return totalDuration;
    }

    public StudyRecordVO setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
        return this;
    }

    public LocalDateTime lastStudyTime() {
        return lastStudyTime;
    }

    public StudyRecordVO setLastStudyTime(LocalDateTime lastStudyTime) {
        this.lastStudyTime = lastStudyTime;
        return this;
    }

    public Integer status() {
        return status;
    }

    public StudyRecordVO setStatus(Integer status) {
        this.status = status;
        return this;
    }
}
