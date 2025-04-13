package com.cms.reception.service;

import com.cms.reception.entity.Course;
import java.util.List;

public interface CourseService {
    Course createCourse(Course course);
    Course getCourseById(Long id);
    List<Course> getCoursesByTeacherId(Long teacherId);
    List<Course> getCoursesByCategoryId(Long categoryId);
    Course updateCourse(Course course);
    void deleteCourse(Long id);
    
    // 添加搜索课程方法
    List<Course> searchCourses(String keyword);
    
    // 添加获取热门课程方法
    List<Course> findTop5ByPopularity();

    void incrementViewCount(Long courseId);

    List<Course> listCourses(Long categoryId, String status, int page, int pageSize);
    int countCourses(Long categoryId, String status);
} 