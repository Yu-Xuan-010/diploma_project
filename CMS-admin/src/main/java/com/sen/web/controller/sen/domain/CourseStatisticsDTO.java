package com.sen.web.controller.sen.domain;

import lombok.Data;

/**
*&#064;BelongsProject: CMS
*&#064;BelongsPackage: com.sen.web.controller.sen.domain
*&#064;Author: gaogao
*&#064;CreateTime: 2025-03-06  17:04
*&#064;Description: TODO
*&#064;Version: 1.0
*/
@Data
public class CourseStatisticsDTO {
    private String courseName;
    private Integer totalStudents;
    private Integer completedStudents;
    private Double averageProgress;
    private Double averageStudyTime;
    private Integer activeStudents;  // 活跃学生数
    private Double completionRate;   // 完成率
}
