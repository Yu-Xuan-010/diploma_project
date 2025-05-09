package com.cms.reception.service.impl;

import com.cms.reception.entity.Course;
import com.cms.reception.entity.CoursePopularity;
import com.cms.reception.mapper.CourseMapper;
import com.cms.reception.mapper.CoursePopularityMapper;
import com.cms.reception.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CoursePopularityMapper coursePopularityMapper;

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
        if (teacherId == null) {
            log.error("教师ID不能为空");
            throw new IllegalArgumentException("教师ID不能为空");
        }
        log.info("正在获取教师ID为 {} 的课程列表", teacherId);
        List<Course> courses = courseMapper.selectByTeacherId(teacherId);
        log.info("成功获取到 {} 个课程", courses.size());
        return courses;
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
        coursePopularityMapper.incrementViewCount(courseId);
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