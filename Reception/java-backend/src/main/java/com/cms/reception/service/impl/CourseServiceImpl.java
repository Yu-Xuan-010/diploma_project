package com.cms.reception.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cms.reception.dto.CourseDTO;
import com.cms.reception.entity.Course;
import com.cms.reception.mapper.CourseMapper;
import com.cms.reception.service.CourseService;
import com.cms.reception.service.LessonService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class CourseServiceImpl implements CourseService {
    
    @Resource
    private CourseMapper courseMapper;
    
    @Resource
    private LessonService lessonService;
    
    @Override
    public IPage<Course> getCourseList(Integer page, Integer size, Long categoryId, String keyword, String status) {
        Page<Course> pageParam = new Page<>(page, size);
        return courseMapper.selectCoursePage(pageParam, categoryId, keyword, status);
    }
    
    @Override
    public CourseDTO getCourseDetail(Long id) {
        Course course = courseMapper.selectCourseDetail(id);
        if (course == null) {
            return null;
        }
        
        CourseDTO courseDTO = new CourseDTO();
        BeanUtils.copyProperties(course, courseDTO);
        courseDTO.setLessons(lessonService.getLessonsByCourseId(id));
        return courseDTO;
    }
    
    @Override
    @Transactional
    public Course createCourse(Course course) {
        courseMapper.insert(course);
        return course;
    }
    
    @Override
    @Transactional
    public Course updateCourse(Course course) {
        courseMapper.updateById(course);
        return course;
    }
    
    @Override
    @Transactional
    public void deleteCourse(Long id) {
        courseMapper.deleteById(id);
    }
    
    @Override
    @Transactional
    public void updateCourseStatus(Long id, String status) {
        Course course = new Course();
        course.setId(id);
        course.setStatus(status);
        courseMapper.updateById(course);
    }
} 