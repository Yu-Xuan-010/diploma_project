package com.cms.reception.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.reception.entity.Lesson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface LessonMapper extends BaseMapper<Lesson> {
    List<Lesson> selectLessonsByCourseId(@Param("courseId") Long courseId);
    
    Lesson selectLessonDetail(@Param("id") Long id);
} 