package com.sen.web.controller.sen.mapper;

import com.sen.web.controller.sen.domain.CoursePopularityDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CoursePopularityMapper {

    @Select("SELECT " +
            "cp.course_id as courseId, " +
            "cp.course_name as courseName, " +
            "cp.view_count as viewCount, " +
            "cp.student_count as studentCount, " +
            "cp.created_at as createdAt " +
            "FROM course_popularity cp " +
            "JOIN course c ON cp.course_id = c.id " +
            "WHERE c.status = 'approved' " +
            "ORDER BY cp.created_at DESC")
    List<CoursePopularityDTO> getCoursePopularityList();

    @Select("SELECT " +
            "cp.course_id as courseId, " +
            "cp.course_name as courseName, " +
            "SUM(cp.view_count) as viewCount, " +
            "MAX(cp.student_count) as studentCount " +
            "FROM course_popularity cp " +
            "JOIN course c ON cp.course_id = c.id " +
            "WHERE c.status = 'approved' " +
            "GROUP BY cp.course_id, cp.course_name " +
            "ORDER BY viewCount DESC")
    List<CoursePopularityDTO> getCoursePopularityTotal();

} 