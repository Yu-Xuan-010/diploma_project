package com.cms.reception.service;

import com.cms.reception.entity.CourseCommentReply;
import java.util.List;

public interface CourseCommentReplyService {
    List<CourseCommentReply> getRepliesByCommentId(Long commentId);
    
    CourseCommentReply addReply(CourseCommentReply reply);
    
    void deleteReply(Long id);
    
    CourseCommentReply getReplyById(Long id);
} 