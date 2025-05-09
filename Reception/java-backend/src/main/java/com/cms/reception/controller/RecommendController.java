package com.cms.reception.controller;

import com.cms.reception.entity.Course;
import com.cms.reception.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.cms.reception.utils.SecurityUtils.getCurrentUserId;

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
    public List<Course> recommendByStudy() {
        Long userId = getCurrentUserId();
        return courseMapper.findRecommendedByRecentCategories(userId).stream()
                .filter(course -> course.getStatus().equals("approved"))  // 仅显示审核通过的课程
                .collect(Collectors.toList());
    }

    @GetMapping("/high-rated")
    public List<Course> recommendHighRated() {
        return courseMapper.findHighRatedCourses().stream()
                .filter(course -> course.getStatus().equals("approved"))  // 仅显示审核通过的课程
                .collect(Collectors.toList());
    }

    @GetMapping("/hot")
    public List<Course> recommendHotCourses() {
        return courseMapper.findHotCourses().stream()
                .filter(course -> course.getStatus().equals("approved"))  // 仅显示审核通过的课程
                .collect(Collectors.toList());
    }
}

