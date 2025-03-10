package com.sen.web.controller.sen.mapper;

import com.sen.web.controller.sen.domain.CourseComment;
import java.util.List;

/**
 * 课程评论管理Mapper接口
 */
public interface CourseCommentMapper {
    /**
     * 查询课程评论列表
     */
    public List<CourseComment> selectCourseCommentList(CourseComment courseComment);

    /**
     * 查询课程评论详细信息
     */
    public CourseComment selectCourseCommentById(Long id);

    /**
     * 新增课程评论
     */
    public int insertCourseComment(CourseComment courseComment);

    /**
     * 修改课程评论
     */
    public int updateCourseComment(CourseComment courseComment);

    /**
     * 删除课程评论
     */
    public int deleteCourseCommentById(Long id);

    /**
     * 批量删除课程评论
     */
    public int deleteCourseCommentByIds(Long[] ids);
} 