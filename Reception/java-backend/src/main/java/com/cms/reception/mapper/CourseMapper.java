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

    /**
     * 根据教师ID查询课程列表
     * @param teacherId 教师ID
     * @return 课程列表
     */
    List<Course> selectByTeacherId(Long teacherId);

    // CourseMapper.java
    @Select("""
    SELECT c2.*, MAX(r.last_study_time) AS last_study_time
            FROM user_study_record r
            JOIN lesson l ON r.lesson_id = l.id
            JOIN course c1 ON l.course_id = c1.id
            JOIN course c2 ON c2.category_id = c1.category_id
            WHERE r.user_id = #{userId}
            GROUP BY c2.id
            ORDER BY last_study_time DESC
            LIMIT 10
""")
    List<Course> findRecommendedByRecentCategories(@Param("userId") Long userId);


    @Select("""
    SELECT *
    FROM course
    WHERE average_rating >= 4
    ORDER BY average_rating DESC
    LIMIT 10
""")
    List<Course> findHighRatedCourses();

    @Select("""
    SELECT *
    FROM course
    ORDER BY student_count DESC
    LIMIT 10
""")
    List<Course> findHotCourses();

}