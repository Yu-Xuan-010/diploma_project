package com.cms.reception.mapper;

import com.cms.reception.entity.CoursePopularity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CoursePopularityMapper {
    /**
     * 根据课程ID查询热度信息
     */
    CoursePopularity selectByCourseId(Long courseId);

    /**
     * 更新浏览量
     */
    int incrementViewCount(@Param("courseId") Long courseId);

    /**
     * 更新学习人数
     */
    int incrementStudentCount(@Param("courseId") Long courseId);

    /**
     * 获取热门课程列表
     */
    List<CoursePopularity> selectTopByPopularity(@Param("limit") int limit);
} 