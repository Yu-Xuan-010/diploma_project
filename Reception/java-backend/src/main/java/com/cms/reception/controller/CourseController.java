package com.cms.reception.controller;

import com.cms.reception.entity.Course;
import com.cms.reception.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/course")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CourseController {
    
    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCourses(@RequestParam String keyword) {
        try {
            List<Course> courses = courseService.searchCourses(keyword);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            log.error("搜索课程时发生错误", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<Course>> getRecommendedCourses() {
        try {
            List<Course> courses = courseService.findTop5ByPopularity();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            log.error("获取推荐课程时发生错误", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 这里可以添加其他课程相关的接口
    // 例如：获取课程列表、课程详情等
} 