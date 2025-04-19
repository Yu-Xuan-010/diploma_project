package com.cms.reception.mapper;

import com.cms.reception.entity.CourseComment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseCommentMapper {

    List<CourseComment> findByCourseId(@Param("courseId") Long courseId);
    
    int insert(CourseComment comment);

    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
    
    int countByCourseId(@Param("courseId") Long courseId);

    int countByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Long userId);

    @Select("SELECT * FROM course_comment WHERE id = #{id}")
    CourseComment selectById(@Param("id") Long id);

    @Delete("DELETE FROM course_comment WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
} 