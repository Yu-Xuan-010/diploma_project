package com.ruoyi.web.controller.sen.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 课程评论回复对象 course_comment_reply
 */
public class CourseCommentReply extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 回复ID */
    private Long id;

    /** 评论ID */
    @Excel(name = "评论ID")
    private Long commentId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 回复内容 */
    @Excel(name = "回复内容")
    private String replyText;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 用户名称（非数据库字段） */
    @Excel(name = "用户名称")
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
} 