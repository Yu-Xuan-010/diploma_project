package com.cms.reception.controller;

import com.cms.reception.entity.Course;
import com.cms.reception.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * &#064;BelongsProject: diploma_project
 * &#064;BelongsPackage: com.cms.reception.controller
 * &#064;Author: gaogao
 * &#064;CreateTime: 2025-05-09  08:51
 * &#064;Description: TODO
 * &#064;Version: 1.0
 */
@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/by-study")
    public List<Course> recommendByStudy(@RequestParam Long userId) {
        return courseMapper.findRecommendedByRecentCategories(userId);
    }

    @GetMapping("/high-rated")
    public List<Course> recommendHighRated() {
        return courseMapper.findHighRatedCourses();
    }

    @GetMapping("/hot")
    public List<Course> recommendHotCourses() {
        return courseMapper.findHotCourses();
    }
}

