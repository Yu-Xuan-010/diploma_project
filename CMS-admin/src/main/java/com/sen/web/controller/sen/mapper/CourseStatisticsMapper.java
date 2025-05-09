package com.sen.web.controller.sen.mapper;

import com.sen.web.controller.sen.domain.CourseStatisticsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * &#064;BelongsProject: CMS
 * &#064;BelongsPackage: com.sen.web.controller.sen.mapper
 * &#064;Author: gaogao
 * &#064;CreateTime: 2025-03-06  17:06
 * &#064;Description: TODO
 * &#064;Version: 1.0
 */
@Mapper
public interface CourseStatisticsMapper {

    @Select("SELECT\n" +
            "  c.name AS courseName,\n" +
            "  COUNT(DISTINCT usr.user_id) AS totalStudents,\n" +
            "  COUNT(DISTINCT CASE WHEN usr.status = 1 THEN usr.user_id END) AS completedStudents,\n" +
            "  NULL AS averageProgress,\n" +
            "  ROUND(AVG(usr.total_duration), 2) AS averageStudyTime,\n" +
            "  COUNT(DISTINCT CASE WHEN usr.last_study_time >= DATE_SUB(NOW(), INTERVAL 7 DAY) THEN usr.user_id END) AS activeStudents,\n" +
            "  ROUND(\n" +
            "    (COUNT(DISTINCT CASE WHEN usr.status = 1 THEN usr.user_id END) / NULLIF(COUNT(DISTINCT usr.user_id), 0)) * 100,\n" +
            "    2\n" +
            "  ) AS completionRate\n" +
            "FROM course c\n" +
            "LEFT JOIN lesson l ON c.id = l.course_id\n" +
            "LEFT JOIN user_study_record usr ON l.id = usr.lesson_id\n" +
            "WHERE c.status = 'approved'  -- ✅ 只统计审核通过的课程\n" +
            "GROUP BY c.id, c.name;\n")
    List<CourseStatisticsDTO> getCourseStatistics();
}
