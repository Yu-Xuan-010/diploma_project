package com.cms.reception.mapper;

import com.cms.reception.entity.CourseComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CourseCommentMapper {
    List<CourseComment> selectByCourseId(@Param("courseId") Long courseId);
    
    int insert(CourseComment comment);
    
    int deleteById(@Param("id") Long id);
    
    CourseComment selectById(@Param("id") Long id);
    
    boolean existsByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Long userId);
} 