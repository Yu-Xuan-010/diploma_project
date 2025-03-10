package com.sen.web.controller.sen.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.sen.common.annotation.Excel;
import com.sen.common.core.domain.BaseEntity;

/**
 * 学习进度对象 user_progress
 *
 * @author sen
 * @date 2025-03-06
 */
public class UserProgress extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户 ID */
    @Excel(name = "用户 ID")
    private Long userId;

    /** 课程 ID */
    @Excel(name = "课程 ID")
    private Long courseId;

    /** 章节 ID */
    @Excel(name = "章节 ID")
    private Long chapterId;

    /** 学习进度 0-100% */
    @Excel(name = "学习进度 0-100%")
    private BigDecimal progress;

    /** 学习时长（分钟） */
    @Excel(name = "学习时长", readConverterExp = "分=钟")
    private Long studyTime;

    /** 是否完成课程 */
    @Excel(name = "是否完成课程")
    private Integer completed;

    /** 最后学习时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后学习时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastAccessedAt;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedAt;

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
    public void setChapterId(Long chapterId)
    {
        this.chapterId = chapterId;
    }

    public Long getChapterId()
    {
        return chapterId;
    }
    public void setProgress(BigDecimal progress)
    {
        this.progress = progress;
    }

    public BigDecimal getProgress()
    {
        return progress;
    }
    public void setStudyTime(Long studyTime)
    {
        this.studyTime = studyTime;
    }

    public Long getStudyTime()
    {
        return studyTime;
    }
    public void setCompleted(Integer completed)
    {
        this.completed = completed;
    }

    public Integer getCompleted()
    {
        return completed;
    }
    public void setLastAccessedAt(Date lastAccessedAt)
    {
        this.lastAccessedAt = lastAccessedAt;
    }

    public Date getLastAccessedAt()
    {
        return lastAccessedAt;
    }
    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }
    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("courseId", getCourseId())
                .append("chapterId", getChapterId())
                .append("progress", getProgress())
                .append("studyTime", getStudyTime())
                .append("completed", getCompleted())
                .append("lastAccessedAt", getLastAccessedAt())
                .append("createdAt", getCreatedAt())
                .append("updatedAt", getUpdatedAt())
                .toString();
    }
}