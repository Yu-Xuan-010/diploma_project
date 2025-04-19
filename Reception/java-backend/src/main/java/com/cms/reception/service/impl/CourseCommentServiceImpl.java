package com.cms.reception.service.impl;

import com.cms.reception.entity.CourseComment;
import com.cms.reception.mapper.CourseCommentMapper;
import com.cms.reception.service.CourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseCommentServiceImpl implements CourseCommentService {
    
    @Autowired
    private CourseCommentMapper courseCommentMapper;
    
    @Override
    public List<CourseComment> getCommentsByCourseId(Long courseId) {
        return courseCommentMapper.findByCourseId(courseId);
    }
    
    @Override
    public CourseComment addComment(CourseComment comment) {
        courseCommentMapper.insert(comment);
        return comment;
    }
    
    @Override
    public boolean deleteComment(Long id, Long userId) {
        return courseCommentMapper.deleteByIdAndUserId(id, userId) > 0;
    }
    
    @Override
    public int getCommentCount(Long courseId) {
        return courseCommentMapper.countByCourseId(courseId);
    }
    
    @Override
    public boolean hasUserCommented(Long courseId, Long userId) {
        return courseCommentMapper.countByCourseIdAndUserId(courseId, userId) > 0;
    }

    @Override
    public CourseComment getCommentById(Long id) {
        return courseCommentMapper.selectById(id);
    }


    public void deleteComment(Long id) {
        courseCommentMapper.deleteById(id);
    }
} 