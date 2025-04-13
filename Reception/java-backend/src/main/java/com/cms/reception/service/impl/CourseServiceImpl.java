package com.cms.reception.service.impl;

import com.cms.reception.entity.Course;
import com.cms.reception.mapper.CourseMapper;
import com.cms.reception.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> searchCourses(String keyword) {
        return courseMapper.searchCourses(keyword);
    }

    @Override
    public List<Course> findTop5ByPopularity() {
        return courseMapper.findTop5ByPopularity();
    }

    @Override
    public Course createCourse(Course course) {
        courseMapper.insertCourse(course);
        return course;
    }

    @Override
    public Course getCourseById(Long id) {
        return courseMapper.selectCourseById(id);
    }

    @Override
    public List<Course> getCoursesByTeacherId(Long teacherId) {
        return courseMapper.selectCoursesByTeacherId(teacherId);
    }

    @Override
    public List<Course> getCoursesByCategoryId(Long categoryId) {
        return courseMapper.selectCoursesByCategoryId(categoryId);
    }

    @Override
    public Course updateCourse(Course course) {
        courseMapper.updateCourse(course);
        return course;
    }

    @Override
    public void deleteCourse(Long id) {
        courseMapper.deleteCourseById(id);
    }

    @Override
    public void incrementViewCount(Long courseId) {
        Course course = courseMapper.selectCourseById(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        course.setViewCount(course.getViewCount() + 1);
        courseMapper.updateCourse(course);
    }

    @Override
    public List<Course> listCourses(Long categoryId, String status, int page, int pageSize) {
        return courseMapper.selectCourseList(categoryId, status, (page - 1) * pageSize, pageSize);
    }

    @Override
    public int countCourses(Long categoryId, String status) {
        return courseMapper.countCourses(categoryId, status);
    }
} 