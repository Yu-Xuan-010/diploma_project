package com.cms.reception.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cms.reception.dto.CourseDTO;
import com.cms.reception.entity.Course;

public interface CourseService {
    IPage<Course> getCourseList(Integer page, Integer size, Long categoryId, String keyword, String status);
    
    CourseDTO getCourseDetail(Long id);
    
    Course createCourse(Course course);
    
    Course updateCourse(Course course);
    
    void deleteCourse(Long id);
    
    void updateCourseStatus(Long id, String status);
} 