package com.cms.reception.mapper;

import com.cms.reception.entity.Course;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CourseMapper {
    @Select("SELECT c.*, cp.view_count FROM course c " +
            "LEFT JOIN course_popularity cp ON c.id = cp.course_id " +
            "WHERE c.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR c.description LIKE CONCAT('%', #{keyword}, '%') " +
            "ORDER BY cp.view_count DESC")
    List<Course> searchCourses(@Param("keyword") String keyword);

    @Select("SELECT c.*, cp.view_count FROM course c " +
            "LEFT JOIN course_popularity cp ON c.id = cp.course_id " +
            "ORDER BY cp.view_count DESC LIMIT 5")
    List<Course> findTop5ByPopularity();

    void insertCourse(Course course);

    Course selectCourseById(Long id);
    List<Course> selectCoursesByTeacherId(Long teacherId);
    List<Course> selectCoursesByCategoryId(Long categoryId);
    void updateCourse(Course course);
    void deleteCourseById(Long id);

    List<Course> selectCourseList(
        @Param("categoryId") Long categoryId,
        @Param("status") String status,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    int countCourses(
        @Param("categoryId") Long categoryId,
        @Param("status") String status
    );



    List<Course> selectTop5ByPopularity();

    int incrementViewCount(@Param("id") Long id);
}