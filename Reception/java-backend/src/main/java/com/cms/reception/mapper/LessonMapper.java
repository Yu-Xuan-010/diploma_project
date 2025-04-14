package com.cms.reception.mapper;

import com.cms.reception.entity.Lesson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface LessonMapper {
    /**
     * 根据课程ID查询课时列表
     */
    List<Lesson> selectByCourseId(Long courseId);

    /**
     * 根据ID查询课时
     */
    Lesson selectById(Long id);

    /**
     * 插入课时
     */
    int insert(Lesson lesson);

    /**
     * 更新课时
     */
    int update(Lesson lesson);

    /**
     * 删除课时
     */
    int deleteById(Long id);

    /**
     * 获取课程的最大排序号
     */
    Integer getMaxSortOrder(Long courseId);
} 