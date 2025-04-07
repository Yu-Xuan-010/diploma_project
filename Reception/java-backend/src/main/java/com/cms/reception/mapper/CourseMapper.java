package com.cms.reception.mapper;

import com.cms.reception.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CourseMapper {
    @Select("SELECT c.*, cp.view_count FROM course c " +
            "LEFT JOIN course_popularity cp ON c.id = cp.course_id " +
            "WHERE c.title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR c.description LIKE CONCAT('%', #{keyword}, '%') " +
            "ORDER BY cp.view_count DESC")
    List<Course> searchCourses(String keyword);

    @Select("SELECT c.*, cp.view_count FROM course c " +
            "LEFT JOIN course_popularity cp ON c.id = cp.course_id " +
            "ORDER BY cp.view_count DESC LIMIT 5")
    List<Course> findTop5ByPopularity();
} 