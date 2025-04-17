package com.cms.reception.mapper;

import com.cms.reception.entity.CourseCommentReply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CourseCommentReplyMapper {
    List<CourseCommentReply> selectByCommentId(@Param("commentId") Long commentId);
    
    int insert(CourseCommentReply reply);
    
    int deleteById(@Param("id") Long id);
    
    CourseCommentReply selectById(@Param("id") Long id);
} 