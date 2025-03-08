package com.ruoyi.web.controller.sen.mapper;

import com.ruoyi.web.controller.sen.domain.CourseStatisticsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * &#064;BelongsProject: CMS
 * &#064;BelongsPackage: com.ruoyi.web.controller.sen.mapper
 * &#064;Author: gaogao
 * &#064;CreateTime: 2025-03-06  17:06
 * &#064;Description: TODO
 * &#064;Version: 1.0
 */
@Mapper
public interface CourseStatisticsMapper {

    @Select("SELECT " +
            "c.name AS courseName, " +
            "COUNT(DISTINCT up.user_id) AS totalStudents, " +
            "COUNT(DISTINCT CASE WHEN up.progress = 100 THEN up.user_id END) AS completedStudents, " +
            "AVG(up.progress) AS averageProgress, " +
            "AVG(up.study_time) AS averageStudyTime, " +
            "COUNT(DISTINCT CASE WHEN up.last_study_time >= DATE_SUB(NOW(), INTERVAL 7 DAY) THEN up.user_id END) AS activeStudents, " +
            "ROUND((COUNT(DISTINCT CASE WHEN up.progress = 100 THEN up.user_id END) / COUNT(DISTINCT up.user_id)) * 100, 2) AS completionRate " +
            "FROM course c " +
            "LEFT JOIN user_progress up ON c.id = up.course_id " +
            "GROUP BY c.id, c.name")
    List<CourseStatisticsDTO> getCourseStatistics();
}
