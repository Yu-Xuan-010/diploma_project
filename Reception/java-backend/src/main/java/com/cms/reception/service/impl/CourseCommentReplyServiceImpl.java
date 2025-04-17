package com.cms.reception.service.impl;

import com.cms.reception.entity.CourseCommentReply;
import com.cms.reception.mapper.CourseCommentReplyMapper;
import com.cms.reception.service.CourseCommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseCommentReplyServiceImpl implements CourseCommentReplyService {
    
    @Autowired
    private CourseCommentReplyMapper courseCommentReplyMapper;
    
    @Override
    public List<CourseCommentReply> getRepliesByCommentId(Long commentId) {
        return courseCommentReplyMapper.selectByCommentId(commentId);
    }
    
    @Override
    @Transactional
    public CourseCommentReply addReply(CourseCommentReply reply) {
        courseCommentReplyMapper.insert(reply);
        return courseCommentReplyMapper.selectById(reply.getId());
    }
    
    @Override
    @Transactional
    public void deleteReply(Long id) {
        courseCommentReplyMapper.deleteById(id);
    }
    
    @Override
    public CourseCommentReply getReplyById(Long id) {
        return courseCommentReplyMapper.selectById(id);
    }
} 