package com.cms.reception.service.impl;

import com.cms.reception.entity.CourseComment;
import com.cms.reception.mapper.CourseCommentMapper;
import com.cms.reception.service.CourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseCommentServiceImpl implements CourseCommentService {
    
    @Autowired
    private CourseCommentMapper courseCommentMapper;
    
    @Override
    public List<CourseComment> getCommentsByCourseId(Long courseId) {
        return courseCommentMapper.selectByCourseId(courseId);
    }
    
    @Override
    @Transactional
    public CourseComment addComment(CourseComment comment) {
        // 检查用户是否已经评论过
        if (hasUserCommented(comment.getCourseId(), comment.getUserId())) {
            throw new RuntimeException("您已经评论过该课程");
        }
        courseCommentMapper.insert(comment);
        return courseCommentMapper.selectById(comment.getId());
    }
    
    @Override
    @Transactional
    public void deleteComment(Long id) {
        courseCommentMapper.deleteById(id);
    }
    
    @Override
    public CourseComment getCommentById(Long id) {
        return courseCommentMapper.selectById(id);
    }
    
    @Override
    public boolean hasUserCommented(Long courseId, Long userId) {
        return courseCommentMapper.existsByCourseIdAndUserId(courseId, userId);
    }
} 