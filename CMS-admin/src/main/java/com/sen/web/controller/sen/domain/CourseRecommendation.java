package com.sen.web.controller.sen.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.sen.common.annotation.Excel;
import com.sen.common.core.domain.BaseEntity;

/**
 * 课程推荐对象 course_recommendation
 * 
 * @author sen
 * @date 2025-03-10
 */
public class CourseRecommendation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 课程id */
    @Excel(name = "课程id")
    private Long courseId;

    /** 推荐原因 */
    @Excel(name = "推荐原因")
    private String reason;

    /** 推荐时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "推荐时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setCourseId(Long courseId) 
    {
        this.courseId = courseId;
    }

    public Long getCourseId() 
    {
        return courseId;
    }
    public void setReason(String reason) 
    {
        this.reason = reason;
    }

    public String getReason() 
    {
        return reason;
    }
    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("courseId", getCourseId())
            .append("reason", getReason())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
