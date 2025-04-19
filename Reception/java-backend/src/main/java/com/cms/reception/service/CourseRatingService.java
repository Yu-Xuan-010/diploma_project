package com.cms.reception.service;

import com.cms.reception.entity.CourseRating;

public interface CourseRatingService {
    /**
     * 获取用户对课程的评分
     * @param courseId 课程ID
     * @param userId 用户ID
     * @return 评分信息
     */
    CourseRating getRating(Long courseId, Long userId);

    /**
     * 更新或创建评分
     * @param courseId 课程ID
     * @param userId 用户ID
     * @param rating 评分值
     * @return 评分信息
     */
    CourseRating updateRating(Long courseId, Long userId, Integer rating);

    Double getAverageRating(Long courseId);

    int getRatingCount(Long courseId);
}
