package com.cms.reception.entity;

import com.sen.common.annotation.Excel;
import com.sen.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 课程种类对象 category
 * 
 * @author sen
 * @date 2025-03-06
 */
public class Category extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Excel(name = "id")
    private Long id;

    /** 种类名称 */
    @Excel(name = "种类名称")
    private String name;

    /** 父类id */
    @Excel(name = "父类id")
    private Long parentId;

    /** 分类描述 */
    @Excel(name = "分类描述")
    private String description;

    /** 排序 */
    @Excel(name = "排序")
    private Long sortOrder;

    /** 课程列表信息 */
    private List<Course> courseList;

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
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setSortOrder(Long sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder() 
    {
        return sortOrder;
    }

    public List<Course> getCourseList()
    {
        return courseList;
    }

    public void setCourseList(List<Course> courseList)
    {
        this.courseList = courseList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("parentId", getParentId())
            .append("description", getDescription())
            .append("sortOrder", getSortOrder())
            .append("courseList", getCourseList())
            .toString();
    }
}
