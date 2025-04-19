package com.cms.reception.controller;

import com.cms.reception.common.Result;
import com.cms.reception.entity.CourseRating;
import com.cms.reception.service.CourseRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseRatingController {
    
    @Autowired
    private CourseRatingService courseRatingService;
    
    @GetMapping("/{courseId}/rating/user/{userId}")
    public Result<CourseRating> getUserRating(
            @PathVariable Long courseId,
            @PathVariable Long userId) {
        CourseRating rating = courseRatingService.getRating(courseId, userId);
        return Result.success(rating);
    }
    
    @PostMapping("/{courseId}/rating")
    public Result<CourseRating> updateRating(
            @PathVariable Long courseId,
            @RequestBody CourseRating rating) {
        CourseRating updatedRating = courseRatingService.updateRating(
            courseId, 
            rating.getUserId(), 
            rating.getRating()
        );
        return Result.success(updatedRating);
    }
    
    @GetMapping("/{courseId}/rating/average")
    public Result<Double> getAverageRating(@PathVariable Long courseId) {
        Double averageRating = courseRatingService.getAverageRating(courseId);
        return Result.success(averageRating);
    }
    
    @GetMapping("/{courseId}/rating/count")
    public Result<Integer> getRatingCount(@PathVariable Long courseId) {
        int count = courseRatingService.getRatingCount(courseId);
        return Result.success(count);
    }
    
    // 获取当前登录用户ID的方法（需要根据你的用户认证系统实现）
    private Long getCurrentUserId() {
        // TODO: 实现获取当前登录用户ID的逻辑
        return null;
    }
} 