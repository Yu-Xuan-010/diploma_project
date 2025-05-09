package com.sen.web.controller.sen.domain;

import com.sen.common.annotation.Excel;
import com.sen.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 课程评论对象 course_comment
 */
public class CourseComment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 评论ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String content;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 状态（0隐藏 1显示） */
    @Excel(name = "状态", readConverterExp = "0=隐藏,1=显示")
    private Integer status;

    /** 用户名称（非数据库字段） */
    @Excel(name = "用户名称")
    private String userName;

    /** 课程名称（非数据库字段） */
    @Excel(name = "课程名称")
    private String courseName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createTime;
    }

    public void setCreatedAt(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
} 