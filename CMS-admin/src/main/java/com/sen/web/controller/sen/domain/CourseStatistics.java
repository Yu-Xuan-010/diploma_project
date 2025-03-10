package com.sen.web.controller.sen.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.sen.common.annotation.Excel;
import com.sen.common.core.domain.BaseEntity;

/**
 * 统计对象 course_statistics
 *
 * @author sen
 * @date 2025-03-06
 */
public class CourseStatistics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 课程 ID */
    @Excel(name = "课程 ID")
    private Long courseId;

    /** 学习该课程的总人数 */
    @Excel(name = "学习该课程的总人数")
    private Long totalStudents;

    /** 完成课程的学生数 */
    @Excel(name = "完成课程的学生数")
    private Long completedStudents;

    /** 课程的平均学习进度 */
    @Excel(name = "课程的平均学习进度")
    private BigDecimal averageProgress;

    /** 课程的平均学习时长（分钟） */
    @Excel(name = "课程的平均学习时长", readConverterExp = "分=钟")
    private Long averageStudyTime;

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
    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public Long getCourseId()
    {
        return courseId;
    }
    public void setTotalStudents(Long totalStudents)
    {
        this.totalStudents = totalStudents;
    }

    public Long getTotalStudents()
    {
        return totalStudents;
    }
    public void setCompletedStudents(Long completedStudents)
    {
        this.completedStudents = completedStudents;
    }

    public Long getCompletedStudents()
    {
        return completedStudents;
    }
    public void setAverageProgress(BigDecimal averageProgress)
    {
        this.averageProgress = averageProgress;
    }

    public BigDecimal getAverageProgress()
    {
        return averageProgress;
    }
    public void setAverageStudyTime(Long averageStudyTime)
    {
        this.averageStudyTime = averageStudyTime;
    }

    public Long getAverageStudyTime()
    {
        return averageStudyTime;
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
                .append("courseId", getCourseId())
                .append("totalStudents", getTotalStudents())
                .append("completedStudents", getCompletedStudents())
                .append("averageProgress", getAverageProgress())
                .append("averageStudyTime", getAverageStudyTime())
                .append("createdAt", getCreatedAt())
                .append("updatedAt", getUpdatedAt())
                .toString();
    }
}