package com.cms.reception.service;

import com.cms.reception.entity.CourseComment;
import java.util.List;

public interface CourseCommentService {
    List<CourseComment> getCommentsByCourseId(Long courseId);
    
    CourseComment addComment(CourseComment comment);
    
    void deleteComment(Long id);
    
    CourseComment getCommentById(Long id);
    
    boolean hasUserCommented(Long courseId, Long userId);
} 