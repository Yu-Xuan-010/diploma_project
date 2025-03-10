package com.ruoyi.web.controller.sen.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.web.controller.sen.domain.CourseComment;
import com.ruoyi.web.controller.sen.domain.CourseCommentReply;
import com.ruoyi.web.controller.sen.mapper.CourseCommentMapper;
import com.ruoyi.web.controller.sen.mapper.CourseCommentReplyMapper;
import com.ruoyi.web.controller.sen.service.ICourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程评论管理Service实现
 */
@Service
public class CourseCommentServiceImpl implements ICourseCommentService {
    @Autowired
    private CourseCommentMapper courseCommentMapper;

    @Autowired
    private CourseCommentReplyMapper courseCommentReplyMapper;

    /**
     * 查询课程评论列表
     */
    @Override
    public List<CourseComment> selectCourseCommentList(CourseComment courseComment) {
        return courseCommentMapper.selectCourseCommentList(courseComment);
    }

    /**
     * 查询课程评论详细信息
     */
    @Override
    public CourseComment selectCourseCommentById(Long id) {
        return courseCommentMapper.selectCourseCommentById(id);
    }

    /**
     * 新增课程评论
     */
    @Override
    public int insertCourseComment(CourseComment courseComment) {
        courseComment.setCreatedAt(DateUtils.getNowDate());
        return courseCommentMapper.insertCourseComment(courseComment);
    }

    /**
     * 修改课程评论
     */
    @Override
    public int updateCourseComment(CourseComment courseComment) {
        return courseCommentMapper.updateCourseComment(courseComment);
    }

    /**
     * 删除课程评论
     */
    @Override
    @Transactional
    public int deleteCourseCommentById(Long id) {
        // 删除评论的同时删除相关回复
        courseCommentReplyMapper.deleteCourseCommentReplyByCommentId(id);
        return courseCommentMapper.deleteCourseCommentById(id);
    }

    /**
     * 批量删除课程评论
     */
    @Override
    @Transactional
    public int deleteCourseCommentByIds(Long[] ids) {
        // 删除评论的同时删除相关回复
        for (Long id : ids) {
            courseCommentReplyMapper.deleteCourseCommentReplyByCommentId(id);
        }
        return courseCommentMapper.deleteCourseCommentByIds(ids);
    }

    /**
     * 查询评论回复列表
     */
    @Override
    public List<CourseCommentReply> selectCourseCommentReplyList(CourseCommentReply courseCommentReply) {
        return courseCommentReplyMapper.selectCourseCommentReplyList(courseCommentReply);
    }

    /**
     * 根据评论ID查询回复列表
     */
    @Override
    public List<CourseCommentReply> selectCourseCommentReplyByCommentId(Long commentId) {
        return courseCommentReplyMapper.selectCourseCommentReplyByCommentId(commentId);
    }

    /**
     * 新增评论回复
     */
    @Override
    public int insertCourseCommentReply(CourseCommentReply courseCommentReply) {
        courseCommentReply.setCreatedAt(DateUtils.getNowDate());
        return courseCommentReplyMapper.insertCourseCommentReply(courseCommentReply);
    }

    /**
     * 删除评论回复
     */
    @Override
    public int deleteCourseCommentReplyById(Long id) {
        return courseCommentReplyMapper.deleteCourseCommentReplyById(id);
    }

    /**
     * 批量删除评论回复
     */
    @Override
    public int deleteCourseCommentReplyByIds(Long[] ids) {
        return courseCommentReplyMapper.deleteCourseCommentReplyByIds(ids);
    }
} 