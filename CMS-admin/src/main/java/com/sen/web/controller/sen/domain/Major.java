package com.sen.web.controller.sen.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.sen.common.annotation.Excel;
import lombok.Data;

import javax.persistence.*;

/**
 * 专业对象 major
 * 
 * @author sen
 */
@Data
@Entity
@Table(name = "major")
public class Major {
    private static final long serialVersionUID = 1L;

    /** 专业ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 专业名称 */
    @Excel(name = "专业名称")
    @Column(name = "name", nullable = false)
    private String name;

    /** 所属学院ID */
    @Excel(name = "所属学院ID")
    @Column(name = "college_id", nullable = false)
    private Long collegeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", insertable = false, updatable = false)
    private College college;

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

    public void setCollegeId(Long collegeId) 
    {
        this.collegeId = collegeId;
    }

    public Long getCollegeId() 
    {
        return collegeId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("collegeId", getCollegeId())
            .toString();
    }
} 