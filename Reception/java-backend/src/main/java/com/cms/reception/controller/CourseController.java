package com.cms.reception.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cms.reception.common.Result;
import com.cms.reception.dto.CourseDTO;
import com.cms.reception.entity.Course;
import com.cms.reception.service.CourseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    @Resource
    private CourseService courseService;
    
    @GetMapping
    public Result<IPage<Course>> list(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(required = false) Long categoryId,
                                    @RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) String status) {
        return Result.success(courseService.getCourseList(page, size, categoryId, keyword, status));
    }
    
    @GetMapping("/{id}")
    public Result<CourseDTO> detail(@PathVariable Long id) {
        return Result.success(courseService.getCourseDetail(id));
    }
    
    @PostMapping
    public Result<Course> create(@RequestBody Course course) {
        return Result.success(courseService.createCourse(course));
    }
    
    @PutMapping("/{id}")
    public Result<Course> update(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        return Result.success(courseService.updateCourse(course));
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return Result.success();
    }
    
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        courseService.updateCourseStatus(id, status);
        return Result.success();
    }
} 