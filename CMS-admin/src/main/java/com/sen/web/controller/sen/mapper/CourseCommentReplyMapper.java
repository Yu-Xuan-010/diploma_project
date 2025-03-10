package com.sen.web.controller.sen.mapper;

import com.sen.web.controller.sen.domain.CourseCommentReply;
import java.util.List;

/**
 * 课程评论回复Mapper接口
 */
public interface CourseCommentReplyMapper {
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

    /**
     * 根据评论ID删除回复
     */
    public int deleteCourseCommentReplyByCommentId(Long commentId);
} 