package com.cms.reception.entity;

import com.sen.common.annotation.Excel;
import com.sen.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDateTime;

/**
 * 课程列表对象 course
 * 
 * @author sen
 * @date 2025-03-05
 */
@Data
public class Course extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String name;

    /** 课程描述 */
    @Excel(name = "课程描述")
    private String description;

    /** 课程种类 */
    @Excel(name = "课程种类")
    private Long categoryId;

    /** 封面 */
    private String image;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;

    /** 评分 */
    private BigDecimal averageRating;

    /** 状态 */
    private String status;

    /** 教师ID */
    private Long teacherId;

    /** 学习人数 */
    private Long studentCount;

    /** 可选：分类名称（需要联表查询时使用） */
    private String categoryName;

    /** 课程内容 */
    private String content;

    /** 课程状态 */
    private Integer statusCode;  // 0: 草稿, 1: 已发布

    /** 课程创建时间 */
    private Date createTime;

    /** 课程更新时间 */
    private Date updateTime;

    /** 课程浏览次数 */
    private Integer viewCount;  // 从 course_popularity 表中获取

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }
    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
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
    public void setAverageRating(BigDecimal averageRating) 
    {
        this.averageRating = averageRating;
    }

    public BigDecimal getAverageRating() 
    {
        return averageRating;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setTeacherId(Long teacherId) 
    {
        this.teacherId = teacherId;
    }

    public Long getTeacherId() 
    {
        return teacherId;
    }
    public void setStudentCount(Long studentCount) 
    {
        this.studentCount = studentCount;
    }

    public Long getStudentCount() 
    {
        return studentCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("description", getDescription())
            .append("categoryId", getCategoryId())
            .append("image", getImage())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .append("averageRating", getAverageRating())
            .append("status", getStatus())
            .append("teacherId", getTeacherId())
            .append("studentCount", getStudentCount())
            .append("categoryName", getCategoryName())
            .append("content", getContent())
            .append("statusCode", getStatusCode())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("viewCount", getViewCount())
            .toString();
    }
}
