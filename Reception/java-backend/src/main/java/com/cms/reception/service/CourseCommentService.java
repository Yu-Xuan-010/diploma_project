package com.cms.reception.service;

import com.cms.reception.entity.CourseComment;
import java.util.List;

public interface CourseCommentService {
    List<CourseComment> getCommentsByCourseId(Long courseId);
    
    CourseComment addComment(CourseComment comment);
    
    boolean deleteComment(Long id, Long userId);
    
    int getCommentCount(Long courseId);
    
    /**
     * 检查用户是否已经评论过该课程
     * @param courseId 课程ID
     * @param userId 用户ID
     * @return 是否已评论
     */
    boolean hasUserCommented(Long courseId, Long userId);
    
    /**
     * 根据ID获取评论
     * @param id 评论ID
     * @return 评论信息
     */
    CourseComment getCommentById(Long id);
} 