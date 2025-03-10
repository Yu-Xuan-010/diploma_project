package com.sen.web.controller.sen.service;

import com.sen.web.controller.sen.domain.CourseComment;
import com.sen.web.controller.sen.domain.CourseCommentReply;
import java.util.List;

/**
 * 课程评论管理Service接口
 */
public interface ICourseCommentService {
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

    /**
     * 查询评论回复列表
     */
    public List<CourseCommentReply> selectCourseCommentReplyList(CourseCommentReply courseCommentReply);

    /**
     * 根据评论ID查询回复列表
     */
    public List<CourseCommentReply> selectCourseCommentReplyByCommentId(Long commentId);

    /**
     * 新增评论回复
     */
    public int insertCourseCommentReply(CourseCommentReply courseCommentReply);

    /**
     * 删除评论回复
     */
    public int deleteCourseCommentReplyById(Long id);

    /**
     * 批量删除评论回复
     */
    public int deleteCourseCommentReplyByIds(Long[] ids);
} 